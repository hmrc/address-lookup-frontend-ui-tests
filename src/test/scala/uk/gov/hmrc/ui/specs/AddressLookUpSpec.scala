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
import uk.gov.hmrc.ui.models.init.{JourneyConfig, JourneyOptions, SelectPageConfig}
import uk.gov.hmrc.ui.pages.{AddressLookUpPage, AddressNotFoundPage, ChooseAddressPage, ConfirmAddressPage, CountrySelectorPage, EditAddressPage}
import uk.gov.hmrc.ui.testdata.Addresses.{MULTIPLE_MATCHES, NO_MATCHES, ORGANISATION, TOO_MANY_MATCHES}

class AddressLookUpSpec extends BaseSpec {

  info("In order to ensure users find the canonical record of their address from the address-lookup service.")
  info("ALF will provide an address search form")

  Feature("Country Lookup page") {
    Scenario("Country name is not duplicated in the country picker") {
      Given("I am on the country selection screen")
      val startPage =
        journeyBuilder.initializeJourney(JourneyConfig(2, JourneyOptions("None", ukMode = Some(false))).asJsonString())
      go to startPage

      When("I have entered a valid country name in the country picker")
      CountrySelectorPage()
        .typeCountryName("France")

      Then("the country name is not duplicated in the picker")
      assert(CountrySelectorPage().getSelectableCountriesCount() == 1)
    }
  }

  Feature("Address Lookup page") {

    Scenario("Access ALF - journey with address lookup page") {
      Given("an initialized ALF journey where the configured journey begins with an address lookup")
      go to journeyBuilder.initializeJourney()

      When("the client application directs the user to the On Ramp url")
      Then("the address lookup form is displayed")
      assert(AddressLookUpPage().isOnPage())
    }

    Scenario("Access ALF - Field validation") {
      Given("an initialized ALF journey where the configured journey begins with an address lookup")
      go to journeyBuilder.initializeJourney()
      assert(AddressLookUpPage().isOnPage(), "Address Lookup page was not displayed")

      When("I do not enter a postcode and submit")
      AddressLookUpPage().clickFindAddress()

      Then("I should see an 'Error Summary' at the top of the page")
      assertErrorMessage("postcode", Some("Enter a UK postcode"))
      assertErrorSummaryLink("postcode", Some("Enter a UK postcode"))
      assertErrorMessageSummaryCountIsEqualTo(1)

      And("I should see an 'Error: ' prefix in the title element")
      assert(AddressLookUpPage().pageTitle.startsWith("Error: "))
    }

    Scenario("Address search - postcode only") {
      Given("I am on the address lookup form")
      val startPage = journeyBuilder.initializeJourney()
      val clientId  = journeyBuilder.getClientID(startPage)
      go to startPage
      assert(AddressLookUpPage().isOnPage())

      When("I search for an address using any postcode")
      AddressLookUpPage()
        .enterPostcode(MULTIPLE_MATCHES.address.postcode)
        .clickFindAddress()

      Then("I am presented with a list of all addresses for the postcode")
      assert(ChooseAddressPage().isOnPage())
      assert(ChooseAddressPage().getAddressesCount(MULTIPLE_MATCHES.address.postcode) >= 1)

      ChooseAddressPage().selectAddress(MULTIPLE_MATCHES.address.asString())
      ChooseAddressPage().clickContinue()
      ConfirmAddressPage().confirmAddress()

      val confirmedAddress = journeyBuilder.getConfirmedAddress(clientId)

      assertThat(confirmedAddress.auditRef).isEqualTo(clientId)
      assertThat(confirmedAddress.id.get).isEqualTo(MULTIPLE_MATCHES.id)
      assertThat(confirmedAddress.address).isEqualTo(MULTIPLE_MATCHES.address.toConfirmedAddress)
    }

    Scenario("Address search - Organisation") {
      Given("I am on the address lookup form")
      val startPage = journeyBuilder.initializeJourney()
      val clientId  = journeyBuilder.getClientID(startPage)
      go to startPage
      assert(AddressLookUpPage().isOnPage())

      When("I search for an organisation address using any postcode")
      AddressLookUpPage()
        .enterPostcode(ORGANISATION.address.postcode)
        .clickFindAddress()

      Then("I am presented with a list of all addresses for the postcode")
      assert(ChooseAddressPage().isOnPage())
      assert(ChooseAddressPage().getAddressesCount(ORGANISATION.address.postcode) >= 1)

      ChooseAddressPage().selectAddress(ORGANISATION.address.organisationName.get)
      ChooseAddressPage().clickContinue()
      ConfirmAddressPage().confirmAddress()

      val confirmedAddress = journeyBuilder.getConfirmedAddress(clientId)

      assertThat(confirmedAddress.auditRef).isEqualTo(clientId)
      assertThat(confirmedAddress.id.get).isEqualTo(ORGANISATION.id)
      assertThat(confirmedAddress.address).isEqualTo(ORGANISATION.address.toConfirmedAddress)
    }

    Scenario("Address search postcode and building name/number") {
      Given("I am on the address lookup form")
      go to journeyBuilder.initializeJourney()
      assert(AddressLookUpPage().isOnPage())

      When("I search for an address using any postcode and a building name / number")
      AddressLookUpPage()
        .enterPostcode(MULTIPLE_MATCHES.address.postcode)
        .enterBuildingNumberOrName("11")
        .clickFindAddress()

      Then("I am presented with a list of addresses that match the postcode and building / name number filter")
      assert(ChooseAddressPage().isOnPage())
      assert(ChooseAddressPage().getAddressesCount(MULTIPLE_MATCHES.address.postcode) >= 1)
    }

    Scenario("Address search - No Results") {
      Given("I have postcode with no associated addresses")
      val postcode = "FX1 7RU"

      And("I am on the address lookup form")
      go to journeyBuilder.initializeJourney()
      assert(AddressLookUpPage().isOnPage())

      When("I search for an address using the postcode with no addresses")
      AddressLookUpPage()
        .enterPostcode(postcode)
        .clickFindAddress()

      Then("I am presented with a message stating there are no search results")
      AddressNotFoundPage().getPageHeading should be(AddressNotFoundPage().constructPostcodeErrorMessageWith(postcode))
    }

    Scenario("Address search - Invalid postcode") {
      Given("I am on the address lookup form")
      go to journeyBuilder.initializeJourney()
      assert(AddressLookUpPage().isOnPage())

      When("I search for an address using an invalid postcode")
      AddressLookUpPage()
        .enterPostcode("ZZZ ZZZddd")
        .clickFindAddress()

      Then("I am presented with a message stating the postcode is invalid")
      assertErrorMessage("postcode", AddressLookUpPage().invalidPostcodeMessage)
      assertErrorMessageSummaryCountIsEqualTo(1)

      And("I should see an 'Error: ' prefix in the title element")
      assert(AddressLookUpPage().pageTitle.contains("Error: "))
    }

    Scenario("Scenario: Address search - Too Many Search Results") {
      Given("I have postcode with more than the configured address search results limit")
      And("I am on the address lookup form")
      val configuration: String = JourneyConfig(
        2,
        JourneyOptions(
          "None",
          ukMode = Some(true),
          selectPageConfig = Some(SelectPageConfig(proposalListLimit = Some(1)))
        )
      ).asJsonString()
      go to journeyBuilder.initializeJourney(configuration)
      assert(AddressLookUpPage().isOnPage())

      When("I search for an address using the postcode with more than the configured address search results limit")
      AddressLookUpPage()
        .enterPostcode(TOO_MANY_MATCHES.address.postcode)
        .clickFindAddress()

      Then("I am presented with a message stating there are too many/more than the configured search results limit")
      AddressLookUpPage().getPageHeading should be(AddressLookUpPage().tooManyAddressesFoundForPostcodeMessage)
    }

    Scenario("Manual Address Entry") {
      Given("I am on the address lookup form")
      go to journeyBuilder.initializeJourney()
      assert(AddressLookUpPage().isOnPage())
      AddressLookUpPage()
        .enterPostcode(NO_MATCHES.postcode)
        .clickFindAddress()

      When("click on the Enter address manually link")
      AddressNotFoundPage().enterAddressManually()

      Then("I am taken to the Enter address manually")
      assert(EditAddressPage().isOnPage())
    }
  }
}
