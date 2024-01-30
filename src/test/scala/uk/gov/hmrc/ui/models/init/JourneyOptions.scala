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

case class JourneyOptions(
  continueUrl: String,
  homeNavHref: Option[String] = None,
  signOutHref: Option[String] = None,
  accessibilityFooterUrl: Option[String] = None,
  additionalStylesheetUrl: Option[String] = None,
  phaseFeedbackLink: Option[String] = None,
  deskProServiceName: Option[String] = None,
  showPhaseBanner: Option[Boolean] = None,
  alphaPhase: Option[Boolean] = None,
  showBackButtons: Option[Boolean] = None,
  disableTranslations: Option[Boolean] = None,
  includeHMRCBranding: Option[Boolean] = None,
  ukMode: Option[Boolean] = None,
  allowedCountryCodes: Option[Set[String]] = None,
  selectPageConfig: Option[SelectPageConfig] = None,
  confirmPageConfig: Option[ConfirmPageConfig] = None,
  timeoutConfig: Option[TimeoutConfig] = None,
  serviceHref: Option[String] = None
)

object JourneyOptions {
  implicit val format: Format[JourneyOptions] = Json.format[JourneyOptions]
}
