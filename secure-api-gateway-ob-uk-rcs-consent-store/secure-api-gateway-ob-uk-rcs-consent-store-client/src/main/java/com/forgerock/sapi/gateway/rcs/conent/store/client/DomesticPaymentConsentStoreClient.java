/*
 * Copyright © 2020-2022 ForgeRock AS (obst@forgerock.com)
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
package com.forgerock.sapi.gateway.rcs.conent.store.client;

import com.forgerock.sapi.gateway.rcs.conent.store.datamodel.payment.domestic.AuthoriseDomesticPaymentConsentRequest;
import com.forgerock.sapi.gateway.rcs.conent.store.datamodel.payment.domestic.ConsumeDomesticPaymentConsentRequest;
import com.forgerock.sapi.gateway.rcs.conent.store.datamodel.payment.domestic.CreateDomesticPaymentConsentRequest;
import com.forgerock.sapi.gateway.rcs.conent.store.datamodel.payment.domestic.DomesticPaymentConsent;
import com.forgerock.sapi.gateway.rcs.conent.store.datamodel.payment.domestic.RejectDomesticPaymentConsentRequest;

/**
 * Client for interacting with com.forgerock.sapi.gateway.rcs.consent.store.api.DomesticPaymentConsentApi
 */
public interface DomesticPaymentConsentStoreClient {

    DomesticPaymentConsent createConsent(CreateDomesticPaymentConsentRequest createConsentRequest) throws ConsentStoreClientException;

    DomesticPaymentConsent getConsent(String consentId, String apiClientId) throws ConsentStoreClientException;

    DomesticPaymentConsent authoriseConsent(AuthoriseDomesticPaymentConsentRequest authoriseDomesticPaymentConsentRequest) throws ConsentStoreClientException;

    DomesticPaymentConsent rejectConsent(RejectDomesticPaymentConsentRequest rejectDomesticPaymentConsentRequest) throws ConsentStoreClientException;

    DomesticPaymentConsent consumeConsent(ConsumeDomesticPaymentConsentRequest consumeDomesticPaymentConsentRequest) throws ConsentStoreClientException;

}
