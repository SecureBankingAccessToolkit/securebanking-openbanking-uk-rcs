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
package com.forgerock.sapi.gateway.rcs.conent.store.datamodel.payment.domestic.v3_1_10;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.forgerock.sapi.gateway.ob.uk.common.datamodel.common.FRCharge;
import com.forgerock.sapi.gateway.ob.uk.common.datamodel.payment.FRWriteDomesticConsent;
import com.forgerock.sapi.gateway.rcs.conent.store.datamodel.BaseCreateConsentRequest;

@Validated
public class CreateDomesticPaymentConsentRequest extends BaseCreateConsentRequest<FRWriteDomesticConsent> {

    @NotNull
    private String idempotencyKey;

    @Valid
    private List<FRCharge> charges;

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public List<FRCharge> getCharges() {
        return charges;
    }

    public void setCharges(List<FRCharge> charges) {
        this.charges = charges;
    }
}
