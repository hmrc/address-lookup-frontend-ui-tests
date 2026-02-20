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

import org.openqa.selenium.support.ui.ExpectedConditions.titleContains
import org.openqa.selenium.WebDriver

case class AddressNotFoundPage() extends BasePage {

  private val postcodeErrorMessagePrefix = "We cannot find any addresses for"

  private lazy val enterAddressManuallyLink: IdQuery   = id("enterManual")
  private lazy val tryDifferentPostcodeButton: IdQuery = id("continue")

  def isOnPage(ukMode: Boolean = false): Boolean =
    webDriverWillWait.until((d: WebDriver) => titleContains("We cannot find any addresses").apply(d).booleanValue())

  def tryADifferentPostcode(): Unit =
    click on tryDifferentPostcodeButton

  def enterAddressManually(): Unit =
    click on enterAddressManuallyLink

  def constructPostcodeErrorMessageWith(postCode: String): String =
    s"$postcodeErrorMessagePrefix $postCode"
}
