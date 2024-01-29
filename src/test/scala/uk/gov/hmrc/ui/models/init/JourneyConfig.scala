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

package uk.gov.hmrc.ui.models.init

import play.api.libs.json.{Format, Json, OFormat}

case class JourneyConfig(
  version: Int,
  options: JourneyOptions,
  labels: Option[JourneyLabels] = Some(JourneyLabels(None)), // messages
  requestedVersion: Option[Int] = None
) {

  def asJsonString(): String =
    Json.toJson(this).toString()
}

object JourneyConfig {
  implicit val labelsFormat: Format[JourneyLabels] = OFormat(JourneyLabels.reads, JourneyLabels.writes)

  implicit val format: Format[JourneyConfig] = Json.format[JourneyConfig]
}
