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

import org.openqa.selenium.support.ui.ExpectedConditions._

case class AddressLookUpPage() extends BasePage {

  val invalidPostcodeMessage: Option[String]          = Some("Enter a real UK Postcode e.g. AA1 1AA")
  val tooManyAddressesFoundForPostcodeMessage: String = "Too many results, enter more details"

  private lazy val buildingNumberOrName: TextField = textField(id("filter"))
  private lazy val postcodeField: TextField        = textField(id("postcode"))
  private lazy val findMyAddress: IdQuery          = id("continue")
  private lazy val enterAddressManually: IdQuery   = id("manualAddress")

  def isOnPage(ukMode: Boolean = true): Boolean =
    webDriverWillWait.until(titleIs(if (ukMode) "Find your UK address" else "Find your address"))

  def clickFindAddress(): AddressLookUpPage = {
    click on findMyAddress
    this
  }

  def enterBuildingNumberOrName(buildingNameOrNumber: String): AddressLookUpPage = {
    buildingNumberOrName.value = buildingNameOrNumber
    this
  }

  def enterPostcode(postcode: String): AddressLookUpPage = {
    postcodeField.value = postcode
    this
  }

  def clickManualEntry(): Unit =
    click on enterAddressManually
}
