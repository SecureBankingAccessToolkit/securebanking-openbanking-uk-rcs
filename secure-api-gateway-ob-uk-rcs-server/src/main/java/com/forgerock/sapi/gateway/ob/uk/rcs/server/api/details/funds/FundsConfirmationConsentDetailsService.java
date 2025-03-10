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
package com.forgerock.sapi.gateway.ob.uk.rcs.server.api.details.funds;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.forgerock.sapi.gateway.ob.uk.common.datamodel.account.FRAccountWithBalance;
import com.forgerock.sapi.gateway.ob.uk.common.datamodel.common.FRAccountIdentifier;
import com.forgerock.sapi.gateway.ob.uk.common.datamodel.funds.FRFundsConfirmationConsentData;
import com.forgerock.sapi.gateway.ob.uk.rcs.api.dto.consent.details.FundsConfirmationConsentDetails;
import com.forgerock.sapi.gateway.ob.uk.rcs.cloud.client.models.ConsentClientDetailsRequest;
import com.forgerock.sapi.gateway.ob.uk.rcs.cloud.client.services.ApiClientServiceClient;
import com.forgerock.sapi.gateway.ob.uk.rcs.server.api.details.BaseConsentDetailsService;
import com.forgerock.sapi.gateway.ob.uk.rcs.server.client.rs.AccountService;
import com.forgerock.sapi.gateway.ob.uk.rcs.server.configuration.ApiProviderConfiguration;
import com.forgerock.sapi.gateway.rcs.consent.store.repo.entity.funds.FundsConfirmationConsentEntity;
import com.forgerock.sapi.gateway.rcs.consent.store.repo.exception.ConsentStoreException;
import com.forgerock.sapi.gateway.rcs.consent.store.repo.service.ConsentService;
import com.forgerock.sapi.gateway.uk.common.shared.api.meta.share.IntentType;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@DependsOn({"internalConsentServices"})
public class FundsConfirmationConsentDetailsService extends BaseConsentDetailsService<FundsConfirmationConsentEntity, FundsConfirmationConsentDetails> {

    private final AccountService accountService;

    public FundsConfirmationConsentDetailsService(
            @Qualifier("internalFundsConfirmationConsentService")ConsentService<FundsConfirmationConsentEntity, ?> consentService,
            ApiProviderConfiguration apiProviderConfiguration,
            ApiClientServiceClient apiClientService,
            AccountService accountService) {
        super(
                IntentType.FUNDS_CONFIRMATION_CONSENT,
                FundsConfirmationConsentDetails::new,
                consentService,
                apiProviderConfiguration,
                apiClientService
        );
        this.accountService = accountService;
    }

    @Override
    protected void addIntentTypeSpecificData(FundsConfirmationConsentDetails consentDetails, FundsConfirmationConsentEntity consent, ConsentClientDetailsRequest consentClientDetailsRequest) {
        final FRFundsConfirmationConsentData readData = consent.getRequestObj().getData();
        final FRAccountIdentifier debtorAccount = readData.getDebtorAccount();

        consentDetails.setExpirationDateTime(readData.getExpirationDateTime());
        consentDetails.setDebtorAccount(debtorAccount);

        addDebtorAccountDetails(consentDetails);
    }

    private void addDebtorAccountDetails(FundsConfirmationConsentDetails consentDetails) {
        final FRAccountIdentifier debtorAccount = consentDetails.getDebtorAccount();
        Objects.requireNonNull(debtorAccount, "The debtor account must be not null to check funds availability.");
        FRAccountWithBalance accountWithBalance = accountService.getAccountWithBalanceByIdentifiers(
                consentDetails.getUserId(), debtorAccount.getIdentification(), debtorAccount.getSchemeName());

        if (Objects.nonNull(accountWithBalance)) {
            debtorAccount.setAccountId(accountWithBalance.getAccount().getAccountId());
            consentDetails.setAccounts(List.of(accountWithBalance));
        } else {
            logger.warn("Failed to set debtorAccount details for consentId: {}, " +
                            "no account found for userId: {}, name:{}, identification: {}, schemeName: {}",
                    consentDetails.getConsentId(), consentDetails.getUserId(), debtorAccount.getName(),
                    debtorAccount.getIdentification(), debtorAccount.getSchemeName());
            throw new ConsentStoreException(ConsentStoreException.ErrorType.INVALID_DEBTOR_ACCOUNT, consentDetails.getConsentId(), "DebtorAccount not found for user");
        }
    }
}
