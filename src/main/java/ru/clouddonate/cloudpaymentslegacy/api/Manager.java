package ru.clouddonate.cloudpaymentslegacy.api;

import lombok.Generated;
import ru.clouddonate.cloudpaymentslegacy.CloudPayments;

public class Manager {
    private final CloudPayments cloudPayments;

    public Manager(CloudPayments cloudPayments) {
        this.cloudPayments = cloudPayments;
    }

    @Generated
    public CloudPayments getCloudPayments() {
        return this.cloudPayments;
    }
}
