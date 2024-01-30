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
import uk.gov.hmrc.ui.models.init.{JourneyConfig, JourneyOptions}
import uk.gov.hmrc.ui.pages.{AddressLookUpPage, ChangeAddressPage, ChooseAddressPage, ConfirmAddressPage, EditAddressPage}
import uk.gov.hmrc.ui.testdata.Addresses.{MULTIPLE_MATCHES, ONE_MATCH}

class AddressConfirmationSpec extends BaseSpec {

  Scenario("Selected address is displayed on confirmation page") {
    Given("Given I am on the address confirmation page")
    go to journeyBuilder.initializeJourney()
    AddressLookUpPage().isOnPage()

    AddressLookUpPage()
      .enterPostcode(MULTIPLE_MATCHES.address.postcode)
      .clickFindAddress()

    assert(ChooseAddressPage().isOnPage(), "Select Address page was not displayed")

    ChooseAddressPage()
      .selectAddress(MULTIPLE_MATCHES.address.lines.head)
      .clickContinue()
    assert(ConfirmAddressPage().isOnPage(), "Confirm Address page was not displayed")

    Then("I can review my selected address before confirming")
    assert(ConfirmAddressPage().addressLineOneField contains MULTIPLE_MATCHES.address.lines.head)
    assert(ConfirmAddressPage().townField contains MULTIPLE_MATCHES.address.town)
    assert(ConfirmAddressPage().postCodeField contains MULTIPLE_MATCHES.address.postcode)
  }

  Scenario("Confirm address") {
    Given("Given I am on the address confirmation page")
    val continueUrl = "http://localhost:9028/admin/metrics"
    val onRampUrl   = journeyBuilder.initializeJourney(
      JourneyConfig(2, JourneyOptions(continueUrl, ukMode = Some(true))).asJsonString()
    )
    go to onRampUrl
    AddressLookUpPage().isOnPage()

    AddressLookUpPage()
      .enterPostcode(MULTIPLE_MATCHES.address.postcode)
      .clickFindAddress()

    assert(ChooseAddressPage().isOnPage(), "Select Address page was not displayed")

    ChooseAddressPage()
      .selectAddress(MULTIPLE_MATCHES.address.lines.head)
      .clickContinue()
    assert(ConfirmAddressPage().isOnPage(), "Confirm Address page was not displayed")

    When("I confirm the address")
    ConfirmAddressPage().confirmAddress()

    Then("I am taken to the Off Ramp url from the frontend client app")
    assert(currentUrl == journeyBuilder.getOffRampUrl(onRampUrl, continueUrl))
  }

  Scenario("Initialized journey navigates directly to confirm page should be able to search and select an address") {
    Given("I have an initialized ALF Journey")
    go to journeyBuilder.initializeJourney()
    AddressLookUpPage().isOnPage()

    And("I navigate to the Address Confirmation page")
    AddressLookUpPage()
      .enterPostcode(ONE_MATCH.address.postcode)
      .clickFindAddress()

    When("I click on the change address link")
    ConfirmAddressPage().changeAddress()

    Then("the change address page is displayed")
    assert(ChangeAddressPage().isOnPage())
  }

  Scenario("Address Confirmation - back link") {
    Given("I am on the Address Confirmation page")
    go to journeyBuilder.initializeJourney()
    AddressLookUpPage().isOnPage()

    AddressLookUpPage()
      .enterPostcode(MULTIPLE_MATCHES.address.postcode)
      .clickFindAddress()

    assert(ChooseAddressPage().isOnPage(), "Select Address page was not displayed")

    ChooseAddressPage()
      .selectAddress(MULTIPLE_MATCHES.address.lines.head)
      .clickContinue()
    assert(ConfirmAddressPage().isOnPage(), "Confirm Address page was not displayed")

    When("I click on the back link")
    ConfirmAddressPage().clickBackLink()

    Then("I should be taken back to the Select Address page")
    assert(ChooseAddressPage().isOnPage(), "Select Address page was not displayed")
  }

  // TODO this scenario is really a confirm page check, should probably move into AddressConfirmationSpec
  Scenario("Confirm address page, click edit address and then go back") {
    Given("I am on the confirm address screen")
    go to journeyBuilder.initializeJourney()
    AddressLookUpPage()
      .enterPostcode(ONE_MATCH.address.postcode)
      .clickFindAddress()
    assertThat(ConfirmAddressPage().isOnPage()).isTrue

    When("I edit my address")
    ConfirmAddressPage().changeAddress()

    Then("I am taken to the Edit address page with my address details prepopulated")
    assertThat(EditAddressPage().isOnPage()).isTrue
    assertThat(EditAddressPage().addressLineOneField.value).isEqualTo(ONE_MATCH.address.lines.head)
    assertThat(EditAddressPage().addressLineTwoField.value).isEqualTo("")
    assertThat(EditAddressPage().addressLineThreeField.value).isEqualTo("")
    assertThat(EditAddressPage().townField.value).isEqualTo(ONE_MATCH.address.town)
    assertThat(EditAddressPage().postcodeField.value).isEqualTo(ONE_MATCH.address.postcode)
    assertThat(EditAddressPage().countryFieldIsDisplayed()).isFalse

    When("I click on the back link")
    AddressLookUpPage().clickBackLink()

    Then("I should be taken back to the Confirm Address page")
    assertThat(ConfirmAddressPage().isOnPage()).isTrue
  }

  Scenario("Search again") {
    Given("I am on the Address Confirmation page")
    go to journeyBuilder.initializeJourney()
    AddressLookUpPage().isOnPage()

    AddressLookUpPage()
      .enterPostcode(MULTIPLE_MATCHES.address.postcode)
      .clickFindAddress()

    assert(ChooseAddressPage().isOnPage(), "Select Address page was not displayed")

    ChooseAddressPage()
      .selectAddress(MULTIPLE_MATCHES.address.lines.head)
      .clickContinue()
    assert(ConfirmAddressPage().isOnPage(), "Confirm Address page was not displayed")

    When("I click change address")
    ConfirmAddressPage().changeAddress()

    Then("I should be taken to the Change address page")
    assert(
      ChangeAddressPage().isOnPage(),
      "Change address page was not displayed after clicking on 'Search again' link"
    )
  }

}
