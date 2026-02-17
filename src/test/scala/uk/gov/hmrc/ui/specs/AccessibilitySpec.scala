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

import uk.gov.hmrc.ui.models.init.{JourneyConfig, JourneyOptions, SelectPageConfig}
import uk.gov.hmrc.ui.pages.{AddressLookUpPage, AddressNotFoundPage, ChooseAddressPage, ConfirmAddressPage, CountrySelectorPage, EditAddressPage}
import uk.gov.hmrc.ui.tags.Accessibility
import uk.gov.hmrc.ui.testdata.Addresses.{MULTIPLE_MATCHES, NO_MATCHES, TOO_MANY_MATCHES}

class AccessibilitySpec extends BaseSpec {

  Scenario("Accessibility - Confirm UK (manual) address", Accessibility) {
    Given("I am on the Address Lookup page")
    go to journeyBuilder.initializeJourney()
    AddressLookUpPage()
      .isOnPage()

    When("I make the address lookup")
    AddressLookUpPage()
      .enterPostcode(MULTIPLE_MATCHES.address.postcode)
      .clickFindAddress()

    assert(ChooseAddressPage().isOnPage(), "Select Address page was not displayed")

    ChooseAddressPage()
      .selectAddress(MULTIPLE_MATCHES.address.lines.head)
      .clickContinue()

    Then("I am on the address confirmation page")
    assert(ConfirmAddressPage().isOnPage(), "Confirm Address page was not displayed")
  }

  Scenario("Accessibility - Confirm non-UK (manual) address", Accessibility) {
    Given("I am on the Address Lookup page")
    val configuration: String = JourneyConfig(2, JourneyOptions("None", ukMode = Some(false))).asJsonString()
    go to journeyBuilder.initializeJourney(configuration)
    CountrySelectorPage()
      .isOnPage()

    When("I select a country that we don't hold data for")
    CountrySelectorPage()
      .selectCountry("China")
      .clickNext()

    Then("I am sent to the manual entry page")

    assert(EditAddressPage().isOnPage(), "Edit Address page was not displayed")

    EditAddressPage()
      .enterAddressLineOne("Musée du Louvre")
      .enterTown("Paris")
      .enterPostcode("75058")
      .clickNext()

    Then("I am taken to the Confirmation page")
    assert(ConfirmAddressPage().isOnPage(), "Confirm Address page was not displayed")
  }

  Scenario("Accessibility - Address search - No Results / Too Many results", Accessibility) {
    Given("I have postcode with no associated addresses")

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

    When("I search for an address using the postcode with no addresses")
    AddressLookUpPage()
      .enterPostcode(NO_MATCHES.postcode)
      .clickFindAddress()

    Then("I am presented with a message stating there are no search results")
    AddressNotFoundPage().getPageHeading should be(
      AddressNotFoundPage().constructPostcodeErrorMessageWith(NO_MATCHES.postcode)
    )
    AddressNotFoundPage().clickBackLink()

    When("I search for an address using the postcode with more than the configured address search results limit")
    AddressLookUpPage()
      .enterPostcode(TOO_MANY_MATCHES.address.postcode)
      .clickFindAddress()

    Then("I am presented with a message stating there are too many/more than the configured search results limit")
    AddressLookUpPage().webDriverWillWait.until(
      org.openqa.selenium.support.ui.ExpectedConditions.textToBe(
        org.openqa.selenium.By.id("pageHeading"),
        AddressLookUpPage().tooManyAddressesFoundForPostcodeMessage
      )
    )
    AddressLookUpPage().getPageHeading should be(AddressLookUpPage().tooManyAddressesFoundForPostcodeMessage)
  }

  Scenario("Accessibility - Error Summary", Accessibility) {
    Given("I am on the Address Lookup page")
    go to journeyBuilder.initializeJourney()
    assert(AddressLookUpPage().isOnPage(), "Address Lookup page was not displayed")

    When("I do not specify a postcode for lookup")
    AddressLookUpPage().clickFindAddress()

    Then("I should see an 'Enter postcode' error at the top of the page")
    assertErrorMessage("postcode", Some("Enter a UK postcode"))
    assertErrorSummaryLink("postcode", Some("Enter a UK postcode"))
    assertErrorMessageSummaryCountIsEqualTo(1)
    AddressLookUpPage().clickManualEntry()

    When("I do not submit the required Edit Address fields")
    assert(EditAddressPage().isOnPage(), "Edit Address page was not displayed")
    EditAddressPage().clickNext()

    Then("I should see field level error messages as well as a clickable 'Error Summary' at the top of the page")

    assertErrorSummaryLink("line1", Some("Enter at least one address line or a town"))
    assertErrorMessage("line1", Some("Enter at least one address line or a town"))
    assertErrorBorder("town")
    assertErrorMessageSummaryCountIsEqualTo(1)
  }
}
