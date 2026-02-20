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

import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.{By, WebDriver}

case class ChooseAddressPage() extends BasePage {

  private lazy val continue: Option[Element] = find(id("continue"))

  lazy val enterAddressManually: IdQuery             = id("editAddress")
  lazy val addressNotSelectedError: CssSelectorQuery = cssSelector(".govuk-error-summary__list a")
  lazy val firstAddress: IdQuery                     = id("addressId")

  def isOnPage(ukMode: Boolean = false): Boolean =
    webDriverWillWait.until((d: WebDriver) => java.lang.Boolean.valueOf(d.getTitle == "Choose your address"))

  def getAddressesCount(postcode: String): Int =
    findAll(xpath(s"//*[contains(text(), '$postcode')]")).size

  def selectAddress(address: String): ChooseAddressPage = {
    webDriverWillWait
      .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(s"//*[contains(text(), '$address')]")))
      .click()
    this
  }

  def selectNoneOfThese(): ChooseAddressPage = {
    webDriverWillWait
      .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(s"//*[contains(text(), 'None of these')]")))
      .click()
    this
  }

  def clickContinue(): Unit =
    click on continue.get

  def clickEnterAddressManually(): Unit =
    click on enterAddressManually

  def clickAddressNotSelectedError(): ChooseAddressPage = {
    click on addressNotSelectedError
    this
  }

  def firstAddressEntryIsActiveElement: Boolean =
    firstAddress.findElement.get.underlying.equals(webDriver.switchTo().activeElement())
}
