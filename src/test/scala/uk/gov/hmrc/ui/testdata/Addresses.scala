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

package uk.gov.hmrc.ui.testdata

import uk.gov.hmrc.ui.models.response.{Address, AddressRecord, LocalCustodian, NonUKAddress}

object Addresses {
  val NO_MATCHES: Address = Address(List(""), "", "FX1 7RU")

  val PO_BOX: AddressRecord              = AddressRecord(
    "GB4510732930",
    4510732930L,
    None,
    None,
    Address(List(), "", "PO1 1PO"),
    localCustodian = Some(LocalCustodian(5720, "MERTON")),
    location = Some(List(49.1904423, -2.0964423)),
    administrativeArea = Some("MERTON"),
    poBox = Some("666999")
  )
  val ONE_MATCH: AddressRecord           = AddressRecord(
    "GB690091234501",
    690091234501L,
    None,
    None,
    Address(List("1 Test Street"), "Testtown", "AA00 0AA"),
    localCustodian = Some(LocalCustodian(121, "NORTH SOMERSET")),
    location = Some(List(49.1901234, -2.0961234)),
    administrativeArea = Some("NORTH SOMERSET")
  )
  val ORGANISATION: AddressRecord        = AddressRecord(
    "GB10091912264",
    10091912264L,
    None,
    None,
    Address(
      List("11 Little Balmer", "Buckingham Industrial Estate"),
      "Buckingham",
      "MK18 1TF",
      organisationName = Some("GATES BUILDING CONTRACTORS LTD")
    ),
    localCustodian = Some(LocalCustodian(121, "BUCKINGHAMSHIRE")),
    location = Some(List(51.9865639000000002, -0.979886799999999947)),
    administrativeArea = Some("BUCKINGHAMSHIRE")
  )
  val MULTIPLE_MATCHES: AddressRecord    = AddressRecord(
    "GB990091234615",
    990091234615L,
    None,
    None,
    Address(List("11a Madeup Street"), "Testtown-upon-Test", "FX1 7RR"),
    localCustodian = Some(LocalCustodian(6810, "GWYNEDD")),
    location = Some(List(49.1901223, -2.0961223)),
    administrativeArea = Some("GWYNEDD")
  )
  val TOO_MANY_MATCHES: AddressRecord    = AddressRecord(
    "GB990091234657",
    990091234657L,
    None,
    None,
    Address(List("1 Too many addresses crescent"), "Anytown", "FX2 7SS")
  )
  val BUILDING_NAME_ONLY: AddressRecord  = AddressRecord(
    "GB990091234556",
    990091234556L,
    None,
    None,
    Address(List("The Farm"), "Royal Madeuptown", "ZZ9Z 9TT")
  )
  val INT_ONE_MATCH: NonUKAddress        =
    NonUKAddress("11781", Some("1"), Some("Abri Lane"), district = Some("Pembroke"), postcode = Some("HM02"))
  val INT_MULTIPLE_MATCHES: NonUKAddress =
    NonUKAddress("31072", Some("4"), Some("Addendum Lane South"), district = Some("Pembroke"), postcode = Some("HM07"))
}
