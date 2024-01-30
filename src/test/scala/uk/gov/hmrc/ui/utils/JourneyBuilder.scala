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

import play.api.libs.json._
import uk.gov.hmrc.ui.config.TestConfig
import uk.gov.hmrc.ui.models.confirmed.ConfirmedAddress
import uk.gov.hmrc.ui.models.init.{JourneyConfig, JourneyOptions}

import java.net.URL
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class JourneyBuilder extends HttpClient {

  val defaultConfiguration: String = JourneyConfig(2, JourneyOptions("None", ukMode = Some(true))).asJsonString()

  object HeaderNames {
    val xRequestId  = "X-Request-ID"
    val userAgent   = "User-Agent"
    val contentType = "Content-Type"
  }

  val applicationJson = "application/json"

  def initializeJourney(configuration: String = defaultConfiguration): String = {
    val response = Await.result(
      post(
        s"${TestConfig.apiUrl("address-lookup-frontend")}/v2/init",
        configuration,
        HeaderNames.contentType -> applicationJson
      ),
      10.seconds
    )

    if (response.status.toString.startsWith("2")) {
      // Return lookup screen URL
      response.headers.getOrElse("Location", Seq("")).head
    } else {
      throw new IllegalStateException("Unable to initialize a new journey!")
    }
  }

  def getClientID(onRampUrl: String): String =
    new URL(onRampUrl).getPath.split("/").toSeq(2)

  def getOffRampUrl(onRampUrl: String, continueUrl: String): String = {
    val clientId = getClientID(onRampUrl)
    s"$continueUrl?id=$clientId"
  }

  def getConfirmedAddress(id: String): ConfirmedAddress = {
    val response = Await.result(
      get(s"${TestConfig.apiUrl("address-lookup-frontend")}/confirmed?id=$id"),
      10.seconds
    )

    val payload = response.body
    println(s"RESPONSE: $payload")

    Json.parse(response.body).as[ConfirmedAddress]
  }
}
