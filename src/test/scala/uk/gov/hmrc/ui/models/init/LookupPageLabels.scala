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

import play.api.libs.json.{Format, Json}

case class LookupPageLabels(
  title: Option[String] = None,
  var titleUkMode: Option[String] = None,
  heading: Option[String] = None,
  var headingUkMode: Option[String] = None,
  filterLabel: Option[String] = None,
  postcodeLabel: Option[String] = None,
  var postcodeLabelUkMode: Option[String] = None,
  submitLabel: Option[String] = None,
  noResultsFoundMessage: Option[String] = None,
  resultLimitExceededMessage: Option[String] = None,
  manualAddressLinkText: Option[String] = None
) {

  titleUkMode = titleUkMode.orElse(title)
  headingUkMode = headingUkMode.orElse(heading)
  postcodeLabelUkMode = postcodeLabelUkMode.orElse(postcodeLabel)
}

object LookupPageLabels {
  implicit val format: Format[LookupPageLabels] = Json.format[LookupPageLabels]
}
