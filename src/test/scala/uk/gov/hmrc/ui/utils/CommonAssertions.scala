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

package uk.gov.hmrc.ui.utils

import org.assertj.core.api.Assertions.assertThat
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import uk.gov.hmrc.ui.specs.BaseSpec

trait CommonAssertions {
  this: BaseSpec =>

  def assertChooseAddressErrorMessage(elementIdentifier: String, expectedErrorMessage: Option[String] = None): Unit = {
    val errorMessage = id(s"$elementIdentifier-error")
    webDriverWillWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".govuk-error-summary")))
    expectedErrorMessage match {
      case None =>
        assertThat(errorMessage.findAllElements.isEmpty).isTrue
      case _    =>
        assertThat(errorMessage.findElement.get.isDisplayed).isTrue
        assertThat(errorMessage.findElement.get.text).isEqualTo(s"Error:\n${expectedErrorMessage.get}")
    }
  }

  def assertErrorMessageSummaryCountIsEqualTo(expectedErrorCount: Int): Unit = {
    val errorSummaryCount = cssSelector(".govuk-error-summary__list > li").findAllElements.length
    assertThat(errorSummaryCount).isEqualTo(expectedErrorCount)
  }

  def checkErrorMessages(
    lineOneError: Option[String] = None,
    townError: Option[String] = None,
    postCodeError: Option[String] = None,
    countryError: Option[String] = None
  ): Unit = {
    val expectedErrors =
      Map("line1" -> lineOneError, "town" -> townError, "postcode" -> postCodeError, "countryCode" -> countryError)
    expectedErrors.foreach { case (elementIdentifier, expectedError) =>
      assertErrorSummaryLink(elementIdentifier, expectedError)
      assertErrorMessage(elementIdentifier, expectedError)
    }
  }

  def assertErrorSummaryLink(elementIdentifier: String, expectedErrorMessage: Option[String] = None): Unit = {
    val linkToError = cssSelector(s"a[href*='$elementIdentifier']")
    expectedErrorMessage match {
      case None =>
        assertThat(linkToError.findAllElements.isEmpty).isTrue
      case _    =>
        assertThat(linkToError.findElement.get.isDisplayed).isTrue
        assertThat(linkToError.findElement.get.text).isEqualTo(expectedErrorMessage.get)
    }
  }

  def assertErrorMessage(elementIdentifier: String, expectedErrorMessage: Option[String] = None): Unit = {
    val errorMessage     = id(s"$elementIdentifier-error")
    // TODO clean this up, it may not catch a case where an element doesn't exist when it should and it should also display an error message
    if (id(s"$elementIdentifier").findAllElements.isEmpty) {
      return
    }
    val dataEntryField   = id(s"$elementIdentifier").findElement.get
    val errorBorderClass = "govuk-input--error"
    webDriverWillWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".govuk-error-summary")))
    expectedErrorMessage match {
      case None =>
        assertThat(errorMessage.findAllElements.isEmpty).isTrue
        assertThat(dataEntryField.attribute("class").get).doesNotContain(errorBorderClass)
      case _    =>
        assertThat(errorMessage.findElement.get.isDisplayed).isTrue
        assertThat(errorMessage.findElement.get.text).isEqualTo(s"Error:\n${expectedErrorMessage.get}")
        if (elementIdentifier == "countryCode") {
          assertThat(dataEntryField.underlying.getCssValue("border-color")).isEqualTo("rgb(212, 53, 28)")
        } else {
          assertErrorBorder(elementIdentifier)
        }
    }
  }

  def assertErrorBorder(elementIdentifier: String): Any = {
    val errorBorderClass = "govuk-input--error"
    assertThat(id(s"$elementIdentifier").findElement.get.attribute("class").get).contains(errorBorderClass)
  }
}
