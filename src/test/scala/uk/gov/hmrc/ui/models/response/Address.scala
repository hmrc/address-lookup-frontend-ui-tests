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

import play.api.libs.json.{Json, OWrites, Reads}

//TODO should be GB not UK, requires test data fixes and TAV-403
case class Address(
  lines: List[String],
  town: String,
  postcode: String,
  country: Option[Country] = Some(Country("GB", "United Kingdom")),
  subdivision: Option[Country] = None,
  organisationName: Option[String] = None
) {

  def asString(): String =
    s"${lines.mkString(",")}, $town, $postcode"

  def toConfirmedAddress: uk.gov.hmrc.ui.models.confirmed.Address =
    uk.gov.hmrc.ui.models.confirmed.Address(
      lines :+ town,
      Some(postcode),
      uk.gov.hmrc.ui.models.confirmed.Country(country.get.code, country.get.name),
      organisationName
    )
}

object Address {
  implicit val reads: Reads[Address]    = Json.reads[Address]
  implicit val writes: OWrites[Address] = Json.writes[Address]
}
