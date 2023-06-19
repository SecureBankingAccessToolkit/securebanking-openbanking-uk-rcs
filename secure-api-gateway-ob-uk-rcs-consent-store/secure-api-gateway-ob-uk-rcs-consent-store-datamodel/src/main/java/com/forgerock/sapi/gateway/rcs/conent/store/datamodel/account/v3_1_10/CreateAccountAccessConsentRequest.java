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
package com.forgerock.sapi.gateway.rcs.conent.store.datamodel.account.v3_1_10;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import uk.org.openbanking.datamodel.account.OBReadConsent1;

public class CreateAccountAccessConsentRequest {

    @NotNull
    private String apiClientId;

    @NotNull
    @Valid
    private OBReadConsent1 consentRequest;


    public String getApiClientId() {
        return apiClientId;
    }

    public void setApiClientId(String apiClientId) {
        this.apiClientId = apiClientId;
    }

    public OBReadConsent1 getConsentRequest() {
        return consentRequest;
    }

    public void setConsentRequest(OBReadConsent1 consentRequest) {
        this.consentRequest = consentRequest;
    }
}
