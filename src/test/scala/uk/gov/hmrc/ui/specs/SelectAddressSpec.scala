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
import uk.gov.hmrc.ui.pages.{AddressLookUpPage, AddressNotFoundPage, ChooseAddressPage, EditAddressPage}
import uk.gov.hmrc.ui.testdata.Addresses.{MULTIPLE_MATCHES, NO_MATCHES}

class SelectAddressSpec extends BaseSpec {

  Feature("Check error scenarios") {

    Scenario("Suggested addresses - Error Summary") {
      Given("I am on the suggested addresses screen")
      go to journeyBuilder.initializeJourney()

      AddressLookUpPage()
        .enterPostcode(MULTIPLE_MATCHES.address.postcode)
        .clickFindAddress()

      When("I do not select an address and submit")
      ChooseAddressPage().clickContinue()

      Then("I should see an 'Error Summary' at the top of the page")
      assertErrorSummaryLink("addressId", Some("Select an address"))
      assertErrorMessageSummaryCountIsEqualTo(1)
      assertChooseAddressErrorMessage("addressId", Some("Select an address"))

      And("I should see an 'Error: ' prefix in the title element")
      assertThat(ChooseAddressPage().pageTitle).startsWith("Error: ")
      assertThat(ChooseAddressPage().firstAddressEntryIsActiveElement).isFalse

      When("I click on the error link")
      ChooseAddressPage().clickAddressNotSelectedError()

      Then("I should have focus drawn to the available addresses")
      assertThat(ChooseAddressPage().firstAddressEntryIsActiveElement).isTrue
    }

  }

  Feature("Navigation scenarios") {

    Scenario("TXMNT-645: Can choose manual entry when no addresses are found") {
      Given("I am on the address not found screen")
      go to journeyBuilder.initializeJourney()
      AddressLookUpPage()
        .enterPostcode(NO_MATCHES.postcode)
        .clickFindAddress()
      assertThat(AddressNotFoundPage().isOnPage()).isTrue

      When("I decide to enter my address manually")
      AddressNotFoundPage().enterAddressManually()

      Then("I should see the blank manual address entry page with country select list")
      assertThat(EditAddressPage().isOnPage()).isTrue
      assertThat(EditAddressPage().addressLineOneField.value).isEqualTo("")
      assertThat(EditAddressPage().addressLineTwoField.value).isEqualTo("")
      assertThat(EditAddressPage().townField.value).isEqualTo("")
      assertThat(EditAddressPage().postcodeField.value).isEqualTo("")
      assertThat(EditAddressPage().countryFieldIsDisplayed()).isFalse
    }

    Scenario("Can choose 'Try a different postcode' when no addresses are found") {
      Given("I am on the address not found screen")
      go to journeyBuilder.initializeJourney()
      AddressLookUpPage()
        .enterPostcode(NO_MATCHES.postcode)
        .clickFindAddress()
      assertThat(AddressNotFoundPage().isOnPage()).isTrue

      When("I decide to try a different postcode")
      AddressNotFoundPage().tryADifferentPostcode()

      Then("I should be returned to the address lookup screen")
      assertThat(AddressLookUpPage().isOnPage()).isTrue
    }

    Scenario("TXMNT-721: Select address page - back link") {
      Given("I am on the suggested addresses screen")
      go to journeyBuilder.initializeJourney()
      AddressLookUpPage()
        .enterPostcode(MULTIPLE_MATCHES.address.postcode)
        .clickFindAddress()
      assertThat(ChooseAddressPage().isOnPage()).isTrue

      When("I click on the back link")
      ChooseAddressPage().clickBackLink()

      Then("I should be taken back to the Address Lookup page")
      assertThat(AddressLookUpPage().isOnPage()).isTrue
    }

    Scenario("TXMNT-721 - Select address page, select none of these, then go back") {
      Given("I am on the suggested addresses screen")
      go to journeyBuilder.initializeJourney()
      AddressLookUpPage()
        .enterPostcode(MULTIPLE_MATCHES.address.postcode)
        .clickFindAddress()
      assertThat(ChooseAddressPage().isOnPage()).isTrue

      When("I choose none of these addresses")
      ChooseAddressPage().selectNoneOfThese()
      ChooseAddressPage().clickContinue()

      Then("I am taken to the manual address entry page ")
      assertThat(EditAddressPage().isOnPage()).isTrue

      When("I click on the back link")
      EditAddressPage().clickBackLink()

      Then("I should be taken back to the Select Address page ")
      assertThat(ChooseAddressPage().isOnPage()).isTrue
    }

  }
}
