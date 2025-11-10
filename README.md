**This is the template README. Please update this with project specific content.**

# address-lookup-frontend-ui-tests

<SERVICE_NAME> UI journey tests.

## Pre-requisites

### Services

Start Mongo Docker container as follows:

```bash
docker run --rm -d -p 27017:27017 --name mongo percona/percona-server-mongodb:5.0
```

If you don't have postgres installed locally you can run it in docker using the following command

```bash
docker run -d --rm --name postgresql -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:10.14
```

If you are running postgres natively (not in docker) then ensure that the `POSTGRES_USER` and `POSTGRES_PASSWORD` values above, and the `username` and `password` `sm` settings
below, are set appropriately.

**IMPORTANT**
To start dependent services locally, run the following script:

```bash
./start_services.sh
```

NOTE: The db connection parameters for `address-lookup` service - this is to ensure that it connects to the docker container running locally and to the sidecar container when
running in `jenkins`.


## Tests

Run tests as follows:

* Argument `<browser>` must be `chrome`, `edge`, or `firefox`.
* Argument `<environment>` must be `local`, `dev`, `qa` or `staging`.

```bash
./run-tests.sh <browser> <environment>
```

## Scalafmt

Check all project files are formatted as expected as follows:

```bash
sbt scalafmtCheckAll scalafmtCheck
```

Format `*.sbt` and `project/*.scala` files as follows:

```bash
sbt scalafmtSbt
```

Format all project files as follows:

```bash
sbt scalafmtAll
```

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
