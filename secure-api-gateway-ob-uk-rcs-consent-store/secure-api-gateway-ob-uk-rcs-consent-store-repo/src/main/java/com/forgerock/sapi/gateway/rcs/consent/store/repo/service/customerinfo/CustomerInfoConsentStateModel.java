/*
 * Copyright © 2020-2025 ForgeRock AS (obst@forgerock.com)
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
package com.forgerock.sapi.gateway.rcs.consent.store.repo.service.customerinfo;

import com.forgerock.sapi.gateway.rcs.consent.store.repo.service.ConsentStateModel;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.org.openbanking.datamodel.v3.common.OBExternalRequestStatus1Code;

import java.util.List;

/**
 * State Model for Customer info Consents, following the same status model of {@link com.forgerock.sapi.gateway.rcs.consent.store.repo.service.account.AccountAccessConsentStateModel}
 *
 * Account Access Consents are long-lived, and support re-authentication.
 *
 * Note: When a Consent is revoked (consent was previously authorised), it goes to rejected state. In previous versions of the
 * OBIE API, there was a specific revoked state.
 */
public class CustomerInfoConsentStateModel implements ConsentStateModel {

    public static final String AWAITING_AUTHORISATION = OBExternalRequestStatus1Code.AWAITINGAUTHORISATION.toString();
    public static final String AUTHORISED = OBExternalRequestStatus1Code.AUTHORISED.toString();
    public static final String REJECTED = OBExternalRequestStatus1Code.REJECTED.toString();

    private static final CustomerInfoConsentStateModel INSTANCE = new CustomerInfoConsentStateModel();

    public static CustomerInfoConsentStateModel getInstance() {
        return INSTANCE;
    }

    private final MultiValueMap<String, String> stateTransitions;


    private CustomerInfoConsentStateModel() {
        stateTransitions = new LinkedMultiValueMap<>();
        stateTransitions.addAll(AWAITING_AUTHORISATION, List.of(AUTHORISED, REJECTED));
        stateTransitions.addAll(AUTHORISED, List.of(AUTHORISED, REJECTED)); // Authorised has a self link as consent Re-Authentication is supported
    }

    @Override
    public String getInitialConsentStatus() {
        return AWAITING_AUTHORISATION;
    }

    @Override
    public String getAuthorisedConsentStatus() {
        return AUTHORISED;
    }

    @Override
    public String getRejectedConsentStatus() {
        return REJECTED;
    }

    @Override
    public String getRevokedConsentStatus() {
        return REJECTED;
    }

    @Override
    public MultiValueMap<String, String> getValidStateTransitions() {
        return new LinkedMultiValueMap<>(stateTransitions);
    }
}
