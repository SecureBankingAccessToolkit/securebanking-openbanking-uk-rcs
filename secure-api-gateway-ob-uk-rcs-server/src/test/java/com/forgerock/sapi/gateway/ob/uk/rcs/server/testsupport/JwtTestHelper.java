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
package com.forgerock.sapi.gateway.ob.uk.rcs.server.testsupport;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Helps create JWTs with certain values populated dynamically.
 */
public class JwtTestHelper {

    private static final String AM_URI = "https://iam.dev.forgerock.financial:443/am/oauth2/realms/root/realms/alpha";

    /**
     * Creates a JWT that mimics the value that's sent from the UI when requesting a consent's details, or when the
     * user grants consent.
     * @param clientId The ID of the TPP client.
     * @param intentId The Open Banking intent ID.
     * @param username The Customer's username.
     * @return A {@link String} representation of the consent decision JWT.
     */
    @SneakyThrows
    public static String consentRequestJwt(String clientId, String intentId, String username) {
        String payload = "{\n" +
                "\"clientId\":\"" + clientId + "\"," +
                "\"iss\":\"" + AM_URI + "\"," +
                "\"csrf\":\"Tjgj3HTzRl2vn+TmIlPhizb9gqdfR7Zn2a/r5iH+syA=\"," +
                "\"client_description\":\"\"," +
                "\"aud\":\"forgerock-rcs\"," +
                "\"save_consent_enabled\":true," +
                "\"claims\":{" +
                "\"id_token\":{" +
                "\"acr\":{" +
                "\"value\":\"urn:openbanking:psd2:sca\"," +
                "\"essential\":true" +
                "}," +
                "\"openbanking_intent_id\":{" +
                "\"value\":\"" + intentId + "\"," +
                "\"essential\":true" +
                "}" +
                "}," +
                "\"userinfo\":{" +
                "\"openbanking_intent_id\":{" +
                "\"value\":\"" + intentId + "\"," +
                "\"essential\":true" +
                "}" +
                "}" +
                "}," +
                "\"scopes\":{" +
                "\"accounts\":\"accounts\"," +
                "\"openid\":\"openid\"," +
                "\"payments\":\"payments\"" +
                "}," +
                "\"exp\":1614954054," +
                "\"iat\":1614953874," +
                "\"client_name\":\"bdddb430-3160-4aeb-85b9-ddebcb0b8ba2\"," +
                "\"consentApprovalRedirectUri\":\"" + AM_URI + "?response_type=code%20id_token&client_id=" + clientId +
                "&redirect_uri=https://postman-echo.com/get\"" +
                "," +
                "\"username\":\"" + username + "\"" +
                "}";
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(payload));
        jwsObject.sign(new MACSigner(RandomStringUtils.randomAlphabetic(256)));
        return jwsObject.serialize();
    }
}
