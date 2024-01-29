-- Create a table that looks like the address_lookup view in production so that we can load pre-canned data in to it
DROP VIEW IF EXISTS __schema__.address_lookup;
CREATE TABLE __schema__.address_lookup
(
    uprn                  BIGINT PRIMARY KEY,
    parent_uprn           BIGINT,
    usrn                  BIGINT,
    organisation_name     TEXT,
    line1                 TEXT,
    line2                 TEXT,
    line3                 TEXT,
    subdivision           TEXT,
    country_code          TEXT,
    local_custodian_code  SMALLINT,
    language              TEXT,
    blpu_state            SMALLINT,
    logical_status        SMALLINT,
    posttown              VARCHAR(30),
    postcode              TEXT,
    location              TEXT,
    pobox_number          TEXT,
    local_authority       VARCHAR(30),
    address_lookup_ft_col TSVECTOR
);

DROP FUNCTION IF EXISTS address_lookup_uk_vector_insert() CASCADE;
CREATE FUNCTION address_lookup_uk_vector_insert() RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        new.address_lookup_ft_col = to_tsvector('english'::regconfig, array_to_string(
                ARRAY [
                    NULLIF(replace(btrim((NEW.organisation_name)::text), '""', ''), ''),
                    NULLIF(replace(btrim((NEW.line1)::text), '""', ''), ''),
                    NULLIF(replace(btrim((NEW.line2)::text), '""', ''), ''),
                    NULLIF(replace(btrim((NEW.line3)::text), '""', ''), ''),
                    NULLIF(replace(btrim((NEW.posttown)::text), '""', ''), ''),
                    NULLIF(replace(btrim((NEW.local_authority)::text), '""', ''), ''),
                    NULLIF(replace(btrim((NEW.pobox_number)::text), '""', ''), '')],
                ' '::text)) ;
    END IF;
    RETURN NEW;
END
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER alukvectorinsert BEFORE INSERT ON __schema__.address_lookup
    FOR EACH ROW EXECUTE PROCEDURE address_lookup_uk_vector_insert();

CREATE INDEX IF NOT EXISTS address_lookup_postcode_idx
    ON __schema__.address_lookup (postcode);
CREATE INDEX IF NOT EXISTS address_lookup_posttown_idx
    ON __schema__.address_lookup (posttown);
CREATE INDEX IF NOT EXISTS address_lookup_ft_col_idx
    ON __schema__.address_lookup USING gin (address_lookup_ft_col);

DROP VIEW IF EXISTS public.address_lookup;
CREATE VIEW public.address_lookup AS SELECT * FROM __schema__.address_lookup;
