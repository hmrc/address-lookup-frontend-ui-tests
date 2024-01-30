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

package uk.gov.hmrc.ui.models.response

import play.api.libs.json.{Json, Reads, Writes}

case class NonUKAddress(
  id: String,
  number: Option[String] = None,
  street: Option[String] = None,
  unit: Option[String] = None,
  city: Option[String] = None,
  district: Option[String] = None,
  region: Option[String] = None,
  postcode: Option[String] = None
)

object NonUKAddress {
  implicit val reads: Reads[NonUKAddress]   = Json.reads[NonUKAddress]
  implicit val writes: Writes[NonUKAddress] = Json.writes[NonUKAddress]
}
