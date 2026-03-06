/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.ui.utils

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import doobie.Transactor
import doobie.implicits.*
import doobie.postgres.{PFCM, PHC}
import uk.gov.hmrc.ui.utils.DBHelper.*

import java.io.File
import java.net.URI
import java.nio.file.{Files, StandardOpenOption}
import java.util.UUID

trait PostgresDB {

  protected lazy val tx: Transactor[IO] = {
    println(s""">>> schemaName: $schemaName""")
    for {
      ptx <- createInitialTransactor()
      _   <- initialiseDatabaseSchema()(ptx)
      _   <- initialiseTestData()(ptx)
      stx <- createSchemaTransactor()
    } yield stx
  }.unsafeRunSync()

  private def initialiseDatabaseSchema()(implicit t: Transactor[IO]): IO[Transactor[IO]] = {
    (for {
      _ <- printLine(s""">>> Creating schemaName: $schemaName""")
      _ <- update(s"CREATE SCHEMA IF NOT EXISTS $schemaName")
      _ <- printLine(s""">>> Created schemaName: $schemaName""")

      _ <- printLine(s""">>> Setting search path to $schemaName""")
      _ <- update(s"SET SEARCH_PATH TO $schemaName")
      _ <- printLine(s""">>> Set search path to $schemaName""")

      _   <- printLine(s""">>> Creating UK lookup table""")
      ddl <- classpathResourceURL(s"/data/uk/address_lookup_uk_ddl.sql", this)
      _   <- update(new File(ddl), schemaName)
      _   <- printLine(s""">>> Created UK lookup table""")

      _   <- printLine(s""">>> Creating INT lookup table""")
      ddl <- classpathResourceURL(s"/data/int/address_lookup_int_ddl.sql", this)
      _   <- update(new File(ddl), schemaName)
      _   <- printLine(s""">>> Created INT lookup table""")
    } yield ()).unsafeRunSync()

    IO(t)
  }

  private def initialiseTestData()(implicit t: Transactor[IO]): IO[Transactor[IO]] = {
    (for {
      _   <- printLine(s""">>> Creating UK lookup view and indexes function""")
      sql <- classpathResourceURL(s"/data/uk/address-lookup-data.csv", this)
      _   <- ingestDataFile(s"$schemaName.address_lookup", sql)
      _   <- printLine(s""">>> Created UK lookup view and indexes function""")
    } yield ()).unsafeRunSync()

    (for {
      _   <- printLine(s""">>> Creating INT lookup view and indexes function""")
      sql <- classpathResourceURL(s"/data/int/non-uk-data.csv", this)
      _   <- ingestDataFile(s"$schemaName.bm", sql)
      _   <- printLine(s""">>> Created INT lookup view and indexes function""")
    } yield ()).unsafeRunSync()

    IO(t)
  }

  //  protected val schemaName =
  //    s"test_schema_${LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss_ZZZ"))}"
  protected val schemaName = s"test_schema_${UUID.randomUUID().toString.replaceAll("-", "_")}"

  private val jdbcBaseUrl = "jdbc:postgresql://localhost:5432/"

  private def createInitialTransactor(): IO[Transactor[IO]] =
    createTransactorWith(
      jdbcUrl = s"$jdbcBaseUrl",
      username = "postgres",
      password = "postgres"
    )

  private def createSchemaTransactor(): IO[Transactor[IO]] =
    createTransactorWith(
      jdbcUrl = s"$jdbcBaseUrl?currentSchema=$schemaName",
      username = "postgres",
      password = "postgres"
    )

  private def createTransactorWith(jdbcUrl: String, username: String, password: String): IO[Transactor[IO]] =
    IO {
      val props = new java.util.Properties()
      props.setProperty("user", username)
      props.setProperty("password", password)
      Transactor.fromDriverManager[IO](
        "org.postgresql.Driver",
        jdbcUrl,
        props,
        None
      )
    }

  private def ingestDataFile(table: String, filePath: URI)(implicit tx: Transactor[IO]): IO[Long] = {
    val in = Files.newInputStream(new File(filePath).toPath, StandardOpenOption.READ)
    PHC
      .pgGetCopyAPI(
        PFCM.copyIn(s"""COPY $table FROM STDIN WITH (FORMAT CSV, HEADER, DELIMITER ',');""", in)
      )
      .transact(tx)
  }
  // END
}
