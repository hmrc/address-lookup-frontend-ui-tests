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

import org.openqa.selenium.Keys
import org.openqa.selenium.support.ui.ExpectedConditions.titleIs

case class CountrySelectorPage() extends BasePage {

  private lazy val nextButton: Option[Element] = find(id("continue"))

  lazy val countrySelectionField: SingleSel   = singleSel(id("countryCode-select"))
  lazy val countryField: Option[Element]      = find(id("countryCode"))
  lazy val countryAutoCompleteField: IdQuery  = id("countryCode__option--0")
  lazy val countryCodeLists: CssSelectorQuery = cssSelector("#countryCode__listbox li")

  def isOnPage(ukMode: Boolean = false): Boolean =
    webDriverWillWait.until(titleIs("Select your country"))

  def selectCountry(country: String): CountrySelectorPage = {
    typeCountryName(country)
    action.sendKeys(Keys.TAB).perform()
    this
  }

  def typeCountryName(country: String): CountrySelectorPage = {
    countryField.get.underlying.click()
    action.sendKeys(Keys.DELETE).perform()
    countryField.get.underlying.sendKeys(country)
    this
  }

  def clickNext(): Unit =
    click on nextButton.get

  def getSelectableCountriesCount(): Int =
    findAll(countryCodeLists).size
}
