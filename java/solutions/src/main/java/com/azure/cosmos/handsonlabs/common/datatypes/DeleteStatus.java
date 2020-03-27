// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.handsonlabs.common.datatypes;

public class DeleteStatus {
    private int deleted;
    private boolean continuation;

    public int getDeleted() {
        return deleted;
    }

    public boolean isContinuation() {
        return continuation;
    }

    public void setContinuation(boolean continuation) {
        this.continuation = continuation;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
}