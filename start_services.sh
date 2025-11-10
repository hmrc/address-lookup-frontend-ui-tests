#!/usr/bin/env bash

sm2 --start ADDRESS_LOOKUP_SERVICES --appendArgs '{
    "ADDRESS_LOOKUP":[
        "-Dauditing.enabled=false",
        "-Dmicroservice.services.access-control.enabled=false"
    ],
    "ADDRESS_SEARCH_API": [
        "-Dauditing.enabled=false",
        "-Dmicroservice.address-search-api.rds.enabled=true",
        "-Dmicroservice.address-search-api.rds.url=jdbc:postgresql://localhost:5432/",
        "-Dmicroservice.address-search-api.rds.username=postgres",
        "-Dmicroservice.address-search-api.rds.password=postgres"
    ],
    "ADDRESS_LOOKUP_FRONTEND": [
        "-Dmicroservice.selectPageConfig.showNoneOfTheseOption=true"
    ]
}'