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
package com.forgerock.securebanking.platform.client.test.support;

import com.forgerock.securebanking.platform.client.ConsentStatusCode;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.TimeZone;
import java.util.UUID;

import static java.util.UUID.randomUUID;

/**
 * Test data factory for Domestic Payment Consent Details
 */
public class DomesticStandingOrderConsentDetailsTestFactory {

    public static final Gson gson = new Gson();

    public static JsonObject aValidDomesticStandingOrderConsentDetails() {
        return aValidDomesticStandingOrderConsentDetailsBuilder(randomUUID().toString());
    }

    public static JsonObject aValidDomesticStandingOrderConsentDetails(String consentId) {
        return aValidDomesticStandingOrderConsentDetailsBuilder(consentId);
    }

    public static JsonObject aValidDomesticStandingOrderConsentDetails(String consentId, String clientId) {
        return aValidDomesticStandingOrderConsentDetailsBuilder(consentId, clientId);
    }

    public static JsonObject aValidDomesticStandingOrderConsentDetailsBuilder(String consentId) {
        JsonObject consent = new JsonObject();
        consent.addProperty("id", UUID.randomUUID().toString());
        final JsonObject obIntent = new JsonObject();
        obIntent.add("Data", aValidDomesticStandingOrderConsentDataDetailsBuilder(consentId));
        consent.add("OBIntentObject", obIntent);
        consent.add("resourceOwnerUsername", null);
        consent.addProperty("oauth2ClientId", randomUUID().toString());
        consent.addProperty("oauth2ClientName", "PISP Name");
        return consent;
    }

    public static JsonObject aValidDomesticStandingOrderConsentDetailsBuilder(String consentId, String clientId) {
        JsonObject consent = new JsonObject();
        consent.addProperty("id", UUID.randomUUID().toString());
        final JsonObject obIntent = new JsonObject();
        obIntent.add("Data", aValidDomesticStandingOrderConsentDataDetailsBuilder(consentId));
        consent.add("OBIntentObject", obIntent);
        consent.add("resourceOwnerUsername", null);
        consent.addProperty("oauth2ClientId", clientId);
        consent.addProperty("oauth2ClientName", "PISP Name");
        return consent;
    }

    public static JsonObject aValidDomesticStandingOrderConsentDataDetailsBuilder(String consentId) {
        JsonObject data = new JsonObject();
        data.addProperty("ConsentId", consentId);
        data.addProperty("CreationDateTime", DateTime.now(DateTimeZone.forTimeZone(TimeZone.getDefault())).toString());
        data.addProperty("StatusUpdateDateTime", DateTime.now(DateTimeZone.forTimeZone(TimeZone.getDefault())).toString());
        data.addProperty("Status", ConsentStatusCode.AWAITINGAUTHORISATION.toString());
        data.add("Initiation", aValidFRWriteDomesticDataInitiationBuilder());
        data.add("Charges", aValidChargesBuilder());
        return data;
    }

    public static JsonObject aValidFRWriteDomesticDataInitiationBuilder() {
        JsonObject data = new JsonObject();
        data.add("DebtorAccount", null);
        data.add("CreditorAccount", JsonParser.parseString("{\n" +
                "        \"SchemeName\": \"UK.OBIE.SortCodeAccountNumber\",\n" +
                "        \"Identification\": \"08080021325698\",\n" +
                "        \"Name\": \"ACME Inc\",\n" +
                "        \"SecondaryIdentification\": \"0002\"\n" +
                "      }"));
        data.addProperty("Reference", "FRESCO-101");
        data.add("SupplementaryData", null);
        data.add("FinalPaymentAmount", JsonParser.parseString("{ \"Amount\": '319.91', \"Currency\": 'GBP' }"));
        data.add("FirstPaymentAmount", JsonParser.parseString("{ \"Amount\": '119.91', \"Currency\": 'GBP' }"));
        data.add("RecurringPaymentAmount", JsonParser.parseString("{ \"Amount\": '813.91', \"Currency\": 'GBP' }"));
        data.addProperty("FinalPaymentDateTime", "2022-09-30T15:15:13+00:00");
        data.addProperty("FirstPaymentDateTime", "2022-05-30T15:15:13+00:00");
        data.addProperty("RecurringPaymentDateTime", "2022-06-30T15:15:13+00:00");
        data.addProperty("Frequency", "EvryDay");
        return data;
    }

    public static JsonArray aValidChargesBuilder() {
        JsonArray charges = new JsonArray();
        charges.add(JsonParser.parseString("{\n" +
                "        \"ChargeBearer\": \"BorneByDebtor\",\n" +
                "        \"Type\": \"UK.OBIE.CHAPSOut\",\n" +
                "        \"Amount\": { \"Amount\": '12.91', \"Currency\": 'GBP' }" +
                "      }"));
        charges.add(JsonParser.parseString("{\n" +
                "        \"ChargeBearer\": \"BorneByDebtor\",\n" +
                "        \"Type\": \"UK.OBIE.CHAPSOut\",\n" +
                "        \"Amount\": { \"Amount\": '8.2', \"Currency\": 'USD' }" +
                "      }"));
        return charges;
    }
}
