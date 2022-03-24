/**
 * Copyright © 2020-2021 ForgeRock AS (obst@forgerock.com)
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
package com.forgerock.securebanking.openbanking.uk.rcs.converters;

import com.forgerock.securebanking.openbanking.uk.rcs.api.dto.consent.details.AccountsConsentDetails;
import com.forgerock.securebanking.openbanking.uk.rcs.converters.general.ConsentDetailsBuilderFactory;
import com.forgerock.securebanking.platform.client.exceptions.ExceptionClient;
import com.forgerock.securebanking.platform.client.models.base.ApiClient;
import com.forgerock.securebanking.platform.client.models.base.ConsentRequest;
import com.forgerock.securebanking.platform.client.test.support.ConsentDetailsRequestTestDataFactory;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import static com.forgerock.securebanking.platform.client.test.support.AccountAccessConsentDetailsTestFactory.aValidAccountConsentDetails;
import static com.forgerock.securebanking.platform.client.test.support.ApiClientTestDataFactory.aValidApiClient;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link ConsentDetailsBuilderFactory}
 */
public class ConsentDetailsBuilderFactoryTest {

    
    public void shouldBuildAccountsConsentDetails() throws ExceptionClient {
        // Given
        JsonObject consentDetails = aValidAccountConsentDetails("AAC_asdfasdfasdf");
        ApiClient apiClient = aValidApiClient();
        // When
        ConsentRequest consentDetailsRequest = ConsentDetailsRequestTestDataFactory.aValidAccountConsentDetailsRequest();
        AccountsConsentDetails accountsConsentDetails = (AccountsConsentDetails) ConsentDetailsBuilderFactory.build(consentDetails, consentDetailsRequest, apiClient);
        // Then
        assertThat(accountsConsentDetails.getPermissions()).isEqualTo(consentDetails.getAsJsonObject("data").getAsJsonObject("Permissions"));
        assertThat(accountsConsentDetails.getFromTransaction()).isEqualTo(consentDetails.getAsJsonObject("data").getAsJsonObject("TransactionFromDateTime"));
        assertThat(accountsConsentDetails.getToTransaction()).isEqualTo(consentDetails.getAsJsonObject("data").getAsJsonObject("TransactionToDateTime"));
        assertThat(accountsConsentDetails.getAispName()).isEqualTo(consentDetails.getAsJsonObject("oauth2ClientName"));
        assertThat(accountsConsentDetails.getExpiredDate()).isEqualTo(consentDetails.getAsJsonObject("data").getAsJsonObject("ExpirationDateTime"));
    }
}
