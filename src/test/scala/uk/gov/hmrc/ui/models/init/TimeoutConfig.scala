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

import play.api.libs.functional.syntax.{unlift, _}
import play.api.libs.json.Reads.min
import play.api.libs.json.{Format, JsPath}

case class TimeoutConfig(timeoutAmount: Int, timeoutUrl: String, timeoutKeepAliveUrl: Option[String] = None)

object TimeoutConfig {
  implicit val timeoutFormat: Format[TimeoutConfig] = (
    (JsPath \ "timeoutAmount").format[Int](min(120)) and
      (JsPath \ "timeoutUrl").format[String] and
      (JsPath \ "timeoutKeepAliveUrl").formatNullable[String]
  )(TimeoutConfig.apply, unlift(TimeoutConfig.unapply))
}
