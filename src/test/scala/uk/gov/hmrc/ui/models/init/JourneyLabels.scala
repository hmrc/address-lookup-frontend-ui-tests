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

import play.api.libs.json.{Json, OWrites, Reads, Writes}

case class JourneyLabels(en: Option[LanguageLabels] = None, cy: Option[LanguageLabels] = None) {

  def asJsonString(): String =
    Json.toJson(this).toString()
}

object JourneyLabels {
  implicit val appLevelWrites: Writes[AppLevelLabels]       = Json.writes[AppLevelLabels]
  implicit val selectPageWrites: Writes[SelectPageLabels]   = Json.writes[SelectPageLabels]
  implicit val lookupPageWrites: Writes[LookupPageLabels]   = Json.writes[LookupPageLabels]
  implicit val editPageWrites: Writes[EditPageLabels]       = Json.writes[EditPageLabels]
  implicit val confirmPageWrites: Writes[ConfirmPageLabels] = Json.writes[ConfirmPageLabels]
  implicit val languageLabelsWrites: Writes[LanguageLabels] = Json.writes[LanguageLabels]
  implicit val writes: OWrites[JourneyLabels]               = Json.writes[JourneyLabels]

  implicit val appLevelReads: Reads[AppLevelLabels]       = Json.reads[AppLevelLabels]
  implicit val selectPageReads: Reads[SelectPageLabels]   = Json.reads[SelectPageLabels]
  implicit val lookupPageReads: Reads[LookupPageLabels]   = Json.reads[LookupPageLabels]
  implicit val editPageReads: Reads[EditPageLabels]       = Json.reads[EditPageLabels]
  implicit val confirmPageReads: Reads[ConfirmPageLabels] = Json.reads[ConfirmPageLabels]
  implicit val languageLabelsReads: Reads[LanguageLabels] = Json.reads[LanguageLabels]
  implicit val reads: Reads[JourneyLabels]                = Json.reads[JourneyLabels]
}
