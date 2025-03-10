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
package com.forgerock.sapi.gateway.rcs.consent.store.repo.service.payment.international.v4_0_0;

import static org.assertj.core.api.Assertions.assertThat;

import com.forgerock.sapi.gateway.ob.uk.common.datamodel.common.FRExchangeRateInformation;
import com.forgerock.sapi.gateway.ob.uk.common.datamodel.converter.v4.payment.FRExchangeRateConverter;
import com.forgerock.sapi.gateway.rcs.consent.store.repo.entity.payment.international.BasePaymentConsentEntityWithExchangeRateInformation;
import com.forgerock.sapi.gateway.rcs.consent.store.repo.service.payment.BasePaymentConsentServiceTest;

import uk.org.openbanking.datamodel.v4.payment.OBExchangeRateType;
import uk.org.openbanking.datamodel.v4.payment.OBWriteInternational3DataInitiationExchangeRateInformation;

public abstract class BasePaymentServiceWithExchangeRateInformationTest<T extends BasePaymentConsentEntityWithExchangeRateInformation<?>> extends BasePaymentConsentServiceTest<T> {

    public static FRExchangeRateInformation getExchangeRateInformation(OBWriteInternational3DataInitiationExchangeRateInformation consentRequestExchangeRateInformation) {
        if (consentRequestExchangeRateInformation.getRateType() == OBExchangeRateType.AGREED) {
            return FRExchangeRateConverter.toFRExchangeRateInformation(consentRequestExchangeRateInformation);
        } else {
            throw new UnsupportedOperationException("Test data is only available for AGREED Exchange Rates at the moment");
        }
    }

    @Override
    protected void validateConsentSpecificFields(T expected, T actual) {
        super.validateConsentSpecificFields(expected, actual);
        assertThat(actual.getExchangeRateInformation()).isEqualTo(expected.getExchangeRateInformation());
    }
}
