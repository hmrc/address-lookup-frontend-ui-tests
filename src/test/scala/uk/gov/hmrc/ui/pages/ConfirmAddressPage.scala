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

package uk.gov.hmrc.ui.pages

import org.openqa.selenium.support.ui.ExpectedConditions.titleIs

case class ConfirmAddressPage() extends BasePage {

  private lazy val changeAddressLink      = id("changeLink")
  private lazy val confirmButton: IdQuery = id("continue")

  lazy val addressLineOneField: String = find(id("line1")).get.text
  lazy val townField: String           = find(id("line2")).get.text
  lazy val postCodeField: String       = find(id("postCode")).get.text
  lazy val CountryField: String        = find(id("country")).get.text

  def isOnPage(ukMode: Boolean = false): Boolean =
    webDriverWillWait.until(titleIs("Review and confirm"))

  def confirmAddress(): Unit =
    click on confirmButton

  def changeAddress(): Unit =
    click on changeAddressLink
}
