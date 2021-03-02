/**
 * Copyright © 2020 ForgeRock AS (obst@forgerock.com)
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
package com.forgerock.securebanking.openbanking.uk.rcs.client.idm;

import com.forgerock.securebanking.common.openbanking.uk.forgerock.datamodel.payment.FRDomesticPaymentConsent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DomesticPaymentConsentService implements PaymentConsentService<FRDomesticPaymentConsent> {

    // TODO - implement
    @Override
    public FRDomesticPaymentConsent getConsent(String consentId) {
        // Ideally go via IG
        throw new UnsupportedOperationException("implement me!");
    }

    @Override
    public void updateConsent(FRDomesticPaymentConsent consent) {
        // Ideally go via IG
        throw new UnsupportedOperationException("implement me!");
    }
}
