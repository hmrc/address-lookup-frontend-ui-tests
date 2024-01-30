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

import com.typesafe.scalalogging.LazyLogging
import org.openqa.selenium.{By, WebDriver, WebElement}
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated
import org.openqa.selenium.support.ui.WebDriverWait
import org.scalatestplus.selenium.WebBrowser
import uk.gov.hmrc.selenium.component.PageObject
import uk.gov.hmrc.selenium.webdriver.Driver

import java.time.Duration

trait BasePage extends PageObject with WebBrowser with LazyLogging {
  logger.info(
    s"Instantiating Browser: ${sys.props.getOrElse("browser", "'browser' System property not set. This is required")}"
  )

  private lazy val backLink: CssSelectorQuery   = cssSelector(".govuk-back-link")
  private lazy val pageHeading: Option[Element] = find(id("pageHeading"))

  implicit lazy val webDriver: WebDriver = Driver.instance

  lazy val webDriverWillWait: WebDriverWait =
    new WebDriverWait(webDriver, Duration.ofSeconds(5), Duration.ofMillis(250))
  lazy val action: Actions                  = new Actions(webDriver)

  def errorSummaryLink(element: String): WebElement =
    webDriverWillWait.until(visibilityOfElementLocated(By.cssSelector(s"a[href*='$element']")))

  def errorNotificationField(field: String): WebElement =
    webDriverWillWait.until(visibilityOfElementLocated(By.id(s"$field-error")))

  def getPageHeading: String =
    pageHeading.get.text

  def clickBackLink(): Unit =
    click on backLink
}
