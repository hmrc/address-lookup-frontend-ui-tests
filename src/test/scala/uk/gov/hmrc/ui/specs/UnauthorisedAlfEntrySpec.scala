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

class UnauthorisedAlfEntrySpec extends BaseSpec {

  Feature("Attempting to access ALF without initialising a journey") {

    Scenario("User cannot navigate to Address Lookup page if they have not initialised a journey") {
      Given("Given I navigate to the Address Lookup page")
      go to s"${TestConfig.url("address-lookup-frontend")}/9e7a7626-93dd-470a-8bdd-4032b0e02d1f/lookup"

      Then("I should see a No Journey page")
      assertThat(currentUrl).isEqualTo(s"${TestConfig.url("address-lookup-frontend")}/no-journey")
    }

    Scenario("User cannot navigate to select address page if they have not initialised a journey") {
      Given("Given I navigate to the Address Confirmation page")
      go to s"${TestConfig.url("address-lookup-frontend")}/9e7a7626-93dd-470a-8bdd-4032b0e02d1f/select?postcode=AA00+0AA"

      Then("I should see a No Journey page")
      assertThat(currentUrl).isEqualTo(s"${TestConfig.url("address-lookup-frontend")}/no-journey")
    }

    Scenario("User cannot navigate to Edit Address page if they have not initialised a journey") {
      Given("Given I navigate to the Edit Address page")
      go to s"${TestConfig.url("address-lookup-frontend")}/9e7a7626-93dd-470a-8bdd-4032b0e02d1f/edit"

      Then("I should see a No Journey page")
      assertThat(currentUrl).isEqualTo(s"${TestConfig.url("address-lookup-frontend")}/no-journey")
    }

    Scenario("User cannot navigate to Confirmation page if they have not initialised a journey") {
      Given("Given I navigate to the Confirmation page")
      go to s"${TestConfig.url("address-lookup-frontend")}/9e7a7626-93dd-470a-8bdd-4032b0e02d1f/confirm"

      Then("I should see a No Journey page")
      assertThat(currentUrl).isEqualTo(s"${TestConfig.url("address-lookup-frontend")}/no-journey")
    }
  }
}
