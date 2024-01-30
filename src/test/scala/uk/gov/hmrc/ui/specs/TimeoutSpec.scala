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

package uk.gov.hmrc.ui.specs

import org.assertj.core.api.Assertions.assertThat
import uk.gov.hmrc.ui.config.TestConfig
import uk.gov.hmrc.ui.models.init.{JourneyConfig, JourneyOptions, TimeoutConfig}
import uk.gov.hmrc.ui.pages.{AddressLookUpPage, TimeoutDialoguePartial}

class TimeoutSpec extends BaseSpec {

  Scenario("Timeout Dialogue links to relative link") {
    Given("I am on the Address Lookup page")
    val timeoutRelativePath   = "/test-only/v2/test-setup"
    val configuration: String = JourneyConfig(
      2,
      JourneyOptions(
        "None",
        ukMode = Some(true),
        timeoutConfig = Some(TimeoutConfig(120, s"/lookup-address$timeoutRelativePath"))
      )
    ).asJsonString()
    go to journeyBuilder.initializeJourney(configuration)
    assertThat(AddressLookUpPage().isOnPage()).isTrue

    When("I see the timeout dialogue")
    assertThat(TimeoutDialoguePartial().isVisible).isTrue

    Then("When I click on sign out I'm sent to a relative URL correctly")
    TimeoutDialoguePartial().clickSignOut()
    assertThat(webDriver.getCurrentUrl).isEqualTo(s"${TestConfig.url("address-lookup-frontend")}$timeoutRelativePath")
  }

  Scenario("Timeout URL can not be an absolute URL") {
    val thrown = intercept[Exception] {

      Given("I want to use an absolute timeout URL")

      val timeoutURL            = "http://www.google.co.uk"
      val configuration: String =
        JourneyConfig(2, JourneyOptions("None", timeoutConfig = Some(TimeoutConfig(120, timeoutURL)))).asJsonString()

      When("I attempt to initialise a journey")

      journeyBuilder.initializeJourney(configuration)
    }

    Then("An error is thrown")

    assert(thrown.getMessage === "Unable to initialize a new journey!")
  }

  Scenario("Timeout Dialogue links to an absolute URL on allow list") {
    Given("I am on a Bank Account Verification Frontend page")

    val timeoutURL            = s"${TestConfig.url("address-lookup-frontend")}/test-only/v2/test-setup"
    val configuration: String = JourneyConfig(
      2,
      JourneyOptions("None", ukMode = Some(true), timeoutConfig = Some(TimeoutConfig(120, timeoutURL)))
    ).asJsonString()
    go to journeyBuilder.initializeJourney(configuration)
    assertThat(AddressLookUpPage().isOnPage()).isTrue

    When("I see the timeout dialogue")
    assertThat(TimeoutDialoguePartial().isVisible).isTrue

    Then("I click on sign out and I'm sent to the absolute URL that is on the allow list correctly")

    TimeoutDialoguePartial().clickSignOut()
    assertThat(webDriver.getCurrentUrl).isEqualTo(timeoutURL)
  }

  Scenario("Timeout keep alive URL can not be an absolute URL") {
    val thrown = intercept[Exception] {

      Given("I want to use an absolute timeout keep alive URL")

      val timeoutRelativePath             = "/test-only/v2/test-setup"
      val timeoutKeepAliveUrlAbsolutePath = "http://www.google.co.uk"
      val configuration: String           = JourneyConfig(
        2,
        JourneyOptions(
          "None",
          timeoutConfig =
            Some(TimeoutConfig(120, s"/lookup-address$timeoutRelativePath", Some(timeoutKeepAliveUrlAbsolutePath)))
        )
      ).asJsonString()

      When("I attempt to initialise a journey")

      journeyBuilder.initializeJourney(configuration)
    }

    Then("An error is thrown")

    assert(thrown.getMessage === "Unable to initialize a new journey!")
  }
}
