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
package com.forgerock.securebanking.openbanking.uk.rcs.api.dto.consent.details;

import com.forgerock.securebanking.common.openbanking.uk.forgerock.datamodel.account.FRAccountWithBalance;
import com.forgerock.securebanking.common.openbanking.uk.forgerock.datamodel.account.FRStandingOrderData;
import com.forgerock.securebanking.common.openbanking.uk.forgerock.datamodel.common.FRAmount;
import com.forgerock.securebanking.common.openbanking.uk.forgerock.datamodel.payment.FRWriteDomesticStandingOrderDataInitiation;
import com.forgerock.securebanking.common.openbanking.uk.forgerock.datamodel.payment.FRWriteInternationalStandingOrderData;
import com.forgerock.securebanking.common.openbanking.uk.forgerock.datamodel.payment.FRWriteInternationalStandingOrderDataInitiation;
import com.forgerock.securebanking.openbanking.uk.common.api.meta.forgerock.FRFrequency;
import com.forgerock.securebanking.platform.client.IntentType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.joda.time.DateTime;

import java.util.List;

import static com.forgerock.securebanking.openbanking.uk.rcs.converters.DomesticStandingOrderConsentDetailsConverter.DATE_TIME_FORMATTER;
import static com.forgerock.securebanking.openbanking.uk.rcs.converters.UtilConverter.isNotNull;

/**
 * Models the consent data for an international standing order.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class InternationalStandingOrderConsentDetails extends ConsentDetails {

    private FRWriteInternationalStandingOrderDataInitiation internationalStandingOrder;
    private List<FRAccountWithBalance> accounts;
    private FRAmount charges;
    private String merchantName;
    private DateTime expiredDate;
    private String currencyOfTransfer;
    private String paymentReference;

    public void setInternationalStandingOrder(FRWriteInternationalStandingOrderDataInitiation internationalStandingOrder) {
        this.internationalStandingOrder = internationalStandingOrder;
    }

    @Override
    public IntentType getIntentType() {
        return IntentType.PAYMENT_INTERNATIONAL_STANDING_ORDERS_CONSENT;
    }

    public void setCharges(JsonArray charges) {
        if (!isNotNull(charges)) {
            this.charges = null;
        } else {
            this.charges = new FRAmount();
            Double amount = 0.0;

            for (JsonElement charge : charges) {
                JsonObject chargeAmount = charge.getAsJsonObject().getAsJsonObject("Amount");
                amount += chargeAmount.get("Amount").getAsDouble();
            }

            this.charges.setCurrency(internationalStandingOrder.getInstructedAmount().getCurrency());
            this.charges.setAmount(amount.toString());
        }
    }

    public void setInternationalStandingOrder(JsonElement firstPaymentDateTime, JsonElement finalPaymentDateTime, JsonObject instructedAmount, JsonElement frequency) {
        FRWriteInternationalStandingOrderDataInitiation standingOrderData = new FRWriteInternationalStandingOrderDataInitiation();

        if (isNotNull(firstPaymentDateTime)) {
            standingOrderData.setFirstPaymentDateTime(DATE_TIME_FORMATTER.parseDateTime(firstPaymentDateTime.getAsString()));
        }

        if (isNotNull(finalPaymentDateTime)) {
            standingOrderData.setFinalPaymentDateTime(DATE_TIME_FORMATTER.parseDateTime(finalPaymentDateTime.getAsString()));
        }

        if (isNotNull(instructedAmount)) {
            FRAmount frInstructedAmount = new FRAmount();
            frInstructedAmount.setAmount(isNotNull(instructedAmount.get("Amount")) ? instructedAmount.get("Amount").getAsString() : null);
            frInstructedAmount.setCurrency(isNotNull(instructedAmount.get("Currency")) ? instructedAmount.get("Currency").getAsString() : null);
            standingOrderData.setInstructedAmount(frInstructedAmount);
        }

        if (isNotNull(frequency)) {
            String frequencyType = frequency.getAsString();
            FRFrequency frFrequency = new FRFrequency(frequencyType);
            String sentence = frFrequency.getSentence();
            standingOrderData.setFrequency(sentence);
        }

        this.internationalStandingOrder = standingOrderData;
    }

}
