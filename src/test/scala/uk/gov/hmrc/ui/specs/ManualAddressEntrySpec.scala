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
import uk.gov.hmrc.ui.models.confirmed.{Address, Country}
import uk.gov.hmrc.ui.models.init.{JourneyConfig, JourneyOptions}
import uk.gov.hmrc.ui.pages.{AddressLookUpPage, ConfirmAddressPage, CountrySelectorPage, EditAddressPage}

import scala.util.Random

class ManualAddressEntrySpec extends BaseSpec {

  val UK_ADDRESS: Address              =
    Address(List("10 Springfield Road", "Springfield"), Some("SP1 1AA"), Country("GB", "United Kingdom"))
  val UK_ORGANISATION_ADDRESS: Address = Address(
    List("10 Wunder Drive", "Wunderville"),
    Some("WD1 1WD"),
    Country("GB", "United Kingdom"),
    organisation = Some("Wunder Works Wheels")
  )
  val INT_ADDRESS: Address             = Address(List("Musée du Louvre", "Paris"), Some("75058"), Country("FR", "France"))

  def randomString(chars: Int): String = Random.alphanumeric.take(chars).mkString

  Feature("Valid") {

    Scenario("Can submit a manually entered UK address") {
      Given("I have manually entered a valid UK address")
      val startPage = journeyBuilder.initializeJourney()
      val clientId  = journeyBuilder.getClientID(startPage)
      go to startPage
      AddressLookUpPage().clickManualEntry()
      EditAddressPage()
        .enterAddressLineOne(UK_ADDRESS.lines.head)
        .enterTown(UK_ADDRESS.lines(1))
        .enterPostcode(UK_ADDRESS.postcode.get)

      When("I confirm my address")

      EditAddressPage().clickNext()
      ConfirmAddressPage().confirmAddress()

      Then("The calling service is able to collect the address I confirmed via an API call")

      val confirmedAddress = journeyBuilder.getConfirmedAddress(clientId)

      assertThat(confirmedAddress.auditRef).isEqualTo(clientId)
      assertThat(confirmedAddress.id).isEqualTo(None)
      assertThat(confirmedAddress.address).isEqualTo(UK_ADDRESS)
    }

    Scenario("Can submit a manually entered UK address with an Organisation Name") {
      Given("I have manually entered a valid UK address with an Organisation Name")
      val startPage = journeyBuilder.initializeJourney()
      val clientId  = journeyBuilder.getClientID(startPage)
      go to startPage
      AddressLookUpPage().clickManualEntry()
      EditAddressPage()
        .enterOrganisation(UK_ORGANISATION_ADDRESS.organisation.get)
        .enterAddressLineOne(UK_ORGANISATION_ADDRESS.lines.head)
        .enterTown(UK_ORGANISATION_ADDRESS.lines(1))
        .enterPostcode(UK_ORGANISATION_ADDRESS.postcode.get)

      When("I confirm my address")

      EditAddressPage().clickNext()
      ConfirmAddressPage().confirmAddress()

      Then("The calling service is able to collect the address I confirmed via an API call")

      val confirmedAddress = journeyBuilder.getConfirmedAddress(clientId)

      assertThat(confirmedAddress.auditRef).isEqualTo(clientId)
      assertThat(confirmedAddress.id).isEqualTo(None)
      assertThat(confirmedAddress.address).isEqualTo(UK_ORGANISATION_ADDRESS)
    }

    Scenario("Can submit a manually entered international address") {
      Given("I have manually entered a valid international address")
      val startPage =
        journeyBuilder.initializeJourney(JourneyConfig(2, JourneyOptions("None", ukMode = Some(false))).asJsonString())
      val clientId  = journeyBuilder.getClientID(startPage)
      go to startPage
      CountrySelectorPage()
        .selectCountry(INT_ADDRESS.country.name)
        .clickNext()
      EditAddressPage()
        .enterAddressLineOne(INT_ADDRESS.lines.head)
        .enterTown(INT_ADDRESS.lines(1))
        .enterPostcode(INT_ADDRESS.postcode.get)

      When("I confirm my address")

      EditAddressPage().clickNext()
      ConfirmAddressPage().confirmAddress()

      Then("The calling service is able to collect the address I confirmed via an API call")

      val confirmedAddress = journeyBuilder.getConfirmedAddress(clientId)

      assertThat(confirmedAddress.auditRef).isEqualTo(clientId)
      assertThat(confirmedAddress.id).isEqualTo(None)
      assertThat(confirmedAddress.address).isEqualTo(INT_ADDRESS)
    }

    Scenario("Check manual address entry page defaults - International mode") {
      Given("I have any non-UK address")
      go to journeyBuilder.initializeJourney(
        JourneyConfig(2, JourneyOptions("None", ukMode = Some(false))).asJsonString()
      )

      When("I go to the manual entry page")
      CountrySelectorPage()
        .selectCountry("China")
        .clickNext()

      Then("I should see the blank manual address entry page with a country select list")

      assertThat(EditAddressPage().isOnPage()).isTrue
      assertThat(EditAddressPage().addressLineOneField.value).isEqualTo("")
      assertThat(EditAddressPage().addressLineTwoField.value).isEqualTo("")
      assertThat(EditAddressPage().townField.value).isEqualTo("")
      assertThat(EditAddressPage().postcodeField.value).isEqualTo("")
      assertThat(EditAddressPage().countryFieldIsDisplayed()).isTrue
      assertThat(EditAddressPage().countryFieldIsEnabled()).isFalse

      When("I click on the back link")
      EditAddressPage().clickBackLink()

      Then("I should be taken back to the Select Address page")
      assertThat(CountrySelectorPage().isOnPage()).isTrue
    }

    Scenario("Check manual address entry page defaults - UK mode") {
      Given("I have a UK address but no postcode")
      go to journeyBuilder.initializeJourney(
        JourneyConfig(2, JourneyOptions("None", ukMode = Some(true))).asJsonString()
      )

      When("I go to the manual entry page")
      AddressLookUpPage().clickManualEntry()

      Then("I should see the blank manual address entry page with no country select list")

      assertThat(EditAddressPage().isOnPage()).isTrue
      assertThat(EditAddressPage().addressLineOneField.value).isEqualTo("")
      assertThat(EditAddressPage().addressLineTwoField.value).isEqualTo("")
      assertThat(EditAddressPage().townField.value).isEqualTo("")
      assertThat(EditAddressPage().postcodeField.value).isEqualTo("")
      assertThat(EditAddressPage().countryFieldIsDisplayed()).isFalse

      When("I click on the back link")
      EditAddressPage().clickBackLink()

      Then("I should be taken back to the Select Address page")
      assertThat(AddressLookUpPage().isOnPage()).isTrue
    }

    Scenario("Can submit a manually entered UK address in International mode") {
      Given("I have manually entered a valid UK address")
      val startPage =
        journeyBuilder.initializeJourney(JourneyConfig(2, JourneyOptions("None", ukMode = Some(false))).asJsonString())
      val clientId  = journeyBuilder.getClientID(startPage)
      go to startPage
      When("I go to the manual entry page")
      CountrySelectorPage()
        .selectCountry("United Kingdom")
        .clickNext()

      AddressLookUpPage().clickManualEntry()
      EditAddressPage()
        .enterAddressLineOne(UK_ADDRESS.lines.head)
        .enterTown(UK_ADDRESS.lines(1))
        .enterPostcode(UK_ADDRESS.postcode.get)

      When("I confirm my address")

      EditAddressPage().clickNext()
      ConfirmAddressPage().confirmAddress()

      Then("The calling service is able to collect the address I confirmed via an API call")

      val confirmedAddress = journeyBuilder.getConfirmedAddress(clientId)

      assertThat(confirmedAddress.auditRef).isEqualTo(clientId)
      assertThat(confirmedAddress.id).isEqualTo(None)
      assertThat(confirmedAddress.address).isEqualTo(UK_ADDRESS)
    }

    Scenario("Can change country after manually entering an international address") {
      Given("I have manually entered a valid international address")
      val startPage =
        journeyBuilder.initializeJourney(JourneyConfig(2, JourneyOptions("None", ukMode = Some(false))).asJsonString())
      go to startPage
      CountrySelectorPage()
        .selectCountry(INT_ADDRESS.country.name)
        .clickNext()
      EditAddressPage()
        .enterAddressLineOne(INT_ADDRESS.lines.head)
        .enterTown(INT_ADDRESS.lines(1))
        .enterPostcode(INT_ADDRESS.postcode.get)

      When("I reach the confirm my address screen ")

      EditAddressPage().clickNext()

      Then("I can go back and change the country I have selected")

      ConfirmAddressPage().clickBackLink()
      EditAddressPage().clickBackLink()
      CountrySelectorPage().selectCountry("Bangladesh")

      And("the newly selected country is displayed")

      EditAddressPage().clickNext()

      webDriverWillWait.until((_: org.openqa.selenium.WebDriver) =>
        EditAddressPage().countryFieldValue() == "Bangladesh"
      )
      assertThat(EditAddressPage().countryFieldValue()).isEqualTo("Bangladesh")
    }

  }

  Feature("Invalid") {

    Scenario("Manual address - Error Summary") {
      Given("I am on the Address Lookup page")
      go to journeyBuilder.initializeJourney()
      AddressLookUpPage().clickManualEntry()

      When("I do not submit the required Edit Address fields")
      EditAddressPage().clickNext()

      Then("I should see field level error messages as well as a clickable 'Error Summary' at the top of the page")

      assertErrorSummaryLink("line1", Some("Enter at least one address line or a town"))
      assertErrorMessage("line1", Some("Enter at least one address line or a town"))
      assertErrorBorder("town")
      assertErrorMessageSummaryCountIsEqualTo(1)
    }

    Scenario("Field Validation: Max characters for required fields") {
      Given("I am on the Address Lookup page")
      go to journeyBuilder.initializeJourney()
      AddressLookUpPage().clickManualEntry()

      When("I do not submit the required Edit Address fields")
      EditAddressPage()
        .enterAddressLineOne(randomString(257))
        .enterTown(randomString(257))
        .enterPostcode(randomString(257))
      EditAddressPage().clickNext()

      Then("I should see field level error messages as well as a clickable 'Error Summary' at the top of the page")
      checkErrorMessages(
        lineOneError = Some("The first address line needs to be fewer than 256 characters"),
        townError = Some("The town or city needs to be fewer than 256 characters"),
        postCodeError = Some("Enter a valid postcode")
      )
      assertErrorMessageSummaryCountIsEqualTo(3)

      And("I should see an 'Error: ' prefix in the title element")
      assertThat(AddressLookUpPage().pageTitle).startsWith("Error: ")
    }
  }
}
