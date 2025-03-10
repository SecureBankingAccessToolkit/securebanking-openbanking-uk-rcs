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
package com.forgerock.sapi.gateway.rcs.consent.store.datamodel;

import java.util.Date;

import org.springframework.validation.annotation.Validated;

import com.forgerock.sapi.gateway.uk.common.shared.api.meta.obie.OBVersion;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Base class representing a Consent DTO
 *
 * @param <T> The OBIE schema type of the Consent Request Object
 */
@Validated
public abstract class BaseConsent<T> {

    @NotNull
    private String id;
    @NotNull
    @Valid
    private T requestObj;
    @NotNull
    private OBVersion requestVersion;
    @NotNull
    private String status;
    @NotNull
    private String apiClientId;
    @NotNull
    private Date creationDateTime;
    @NotNull
    private Date statusUpdateDateTime;

    private String resourceOwnerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getRequestObj() {
        return requestObj;
    }

    public void setRequestObj(T requestObj) {
        this.requestObj = requestObj;
    }

    public OBVersion getRequestVersion() {
        return requestVersion;
    }

    public void setRequestVersion(OBVersion requestVersion) {
        this.requestVersion = requestVersion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApiClientId() {
        return apiClientId;
    }

    public void setApiClientId(String apiClientId) {
        this.apiClientId = apiClientId;
    }

    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public Date getStatusUpdateDateTime() {
        return statusUpdateDateTime;
    }

    public void setStatusUpdateDateTime(Date statusUpdateDateTime) {
        this.statusUpdateDateTime = statusUpdateDateTime;
    }

    @Override
    public String toString() {
        return "BaseConsent{" +
                "id='" + id + '\'' +
                ", requestObj=" + requestObj +
                ", requestVersion=" + requestVersion +
                ", status='" + status + '\'' +
                ", apiClientId='" + apiClientId + '\'' +
                ", creationDateTime=" + creationDateTime +
                ", statusUpdateDateTime=" + statusUpdateDateTime +
                ", resourceOwnerId='" + resourceOwnerId + '\'' +
                '}';
    }
}

