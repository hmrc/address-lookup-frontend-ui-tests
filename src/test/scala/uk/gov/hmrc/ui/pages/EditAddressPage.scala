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

import org.openqa.selenium.WebDriver

case class EditAddressPage() extends BasePage {

  private lazy val nextButton: Option[Element]   = find(id("continue"))
  private lazy val countryField: Option[Element] = find(id("countryName"))

  lazy val organisationNameField: TextField = textField(id("organisation"))
  lazy val addressLineOneField: TextField   = textField(id("line1"))
  lazy val addressLineTwoField: TextField   = textField(id("line2"))
  lazy val addressLineThreeField: TextField = textField(id("line3"))
  lazy val townField: TextField             = textField(id("town"))
  lazy val postcodeField: TextField         = textField(id("postcode"))

  def isOnPage(ukMode: Boolean = false): Boolean =
    webDriverWillWait.until((d: WebDriver) => java.lang.Boolean.valueOf(d.getTitle == "Enter your address"))

  def enterOrganisation(organisationName: String): EditAddressPage = {
    organisationNameField.clear()
    organisationNameField.value = organisationName
    this
  }

  def enterAddressLineOne(addressLineOne: String): EditAddressPage = {
    addressLineOneField.clear()
    addressLineOneField.value = addressLineOne
    this
  }

  def enterAddressLineTwo(addressLineTwo: String): EditAddressPage = {
    addressLineTwoField.clear()
    addressLineTwoField.value = addressLineTwo
    this
  }

  def enterAddressLineThree(addressLineThree: String): EditAddressPage = {
    addressLineThreeField.clear()
    addressLineThreeField.value = addressLineThree
    this
  }

  def enterTown(town: String): EditAddressPage = {
    townField.clear()
    townField.value = town
    this
  }

  def enterPostcode(postcode: String): EditAddressPage = {
    postcodeField.clear()
    postcodeField.value = postcode
    this
  }

  def countryFieldIsDisplayed(): Boolean = {
    if (countryField.isEmpty) {
      return false
    }
    countryField.get.isDisplayed
  }
  def countryFieldIsEnabled(): Boolean   = {
    if (countryField.isEmpty) {
      return false
    }
    countryField.get.isEnabled
  }

  def countryFieldValue(): String = {
    if (countryField.isEmpty) {
      return ""
    }
    countryField.get.attribute("value").get
  }

  def clickNext(): Unit =
    click on nextButton.get
}
