-- Create a table that looks like the international lookup view in production so that we can load pre-canned data in to it
DROP VIEW IF EXISTS __schema__.bm;
CREATE TABLE __schema__.bm
(
    uid                   UUID PRIMARY KEY,
    id                    BIGINT,
    hash                  TEXT,
    cip_id                TEXT,
    number                TEXT,
    street                TEXT,
    unit                  TEXT,
    city                  TEXT,
    district              TEXT,
    region                TEXT,
    postcode              TEXT,
    nonuk_address_lookup_ft_col TSVECTOR
);

DROP FUNCTION IF EXISTS address_lookup_int_vector_insert() CASCADE;
CREATE FUNCTION address_lookup_int_vector_insert() RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        new.nonuk_address_lookup_ft_col = to_tsvector('english'::regconfig, array_to_string(
                ARRAY [
                    NULLIF(replace(btrim((NEW.number)::text), '""', ''), ''),
                    NULLIF(replace(btrim((NEW.street)::text), '""', ''), ''),
                    NULLIF(replace(btrim((NEW.cip_id)::text), '""', ''), ''),
                    NULLIF(replace(btrim((NEW.unit)::text), '""', ''), ''),
                    NULLIF(replace(btrim((NEW.city)::text), '""', ''), ''),
                    NULLIF(replace(btrim((NEW.district)::text), '""', ''), ''),
                    NULLIF(replace(btrim((NEW.region)::text), '""', ''), ''),
                    NULLIF(replace(btrim((NEW.postcode)::text), '""', ''), '')],
                ' '::text)) ;
    END IF;
    RETURN NEW;
END
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER alintvectorinsert BEFORE INSERT ON __schema__.bm
    FOR EACH ROW EXECUTE PROCEDURE address_lookup_int_vector_insert();

CREATE INDEX IF NOT EXISTS nonuk_address_lookup_ft_col
    ON __schema__.bm USING gin (nonuk_address_lookup_ft_col);

DROP VIEW IF EXISTS public.bm;
-- CREATE VIEW public.bm AS SELECT * FROM __schema__.bm;
CREATE VIEW public.bm
AS
SELECT uid,
       id,
       hash,
       cip_id,
       number,
       street,
       unit,
       city,
       district,
       region,
       postcode,
       nonuk_address_lookup_ft_col
FROM __schema__.bm;
