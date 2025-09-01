package ru.clouddonate.cloudpaymentslegacy.localstorage;

import ru.clouddonate.cloudpaymentslegacy.CloudPayments;
import ru.clouddonate.cloudpaymentslegacy.api.Manager;
import ru.clouddonate.cloudpaymentslegacy.config.Config;
import ru.clouddonate.cloudpaymentslegacy.file.FileUtil;
import ru.clouddonate.cloudpaymentslegacy.http.GetResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalStorage
        extends Manager {
    public LocalStorage(CloudPayments cloudPayments) {
        super(cloudPayments);
    }

    public void addPayment(GetResult getResult) {
        if (Config.LocalStorage.Payments.enabled)
            FileUtil.appendToFile(this.getCloudPayments().getDataFolder() + "/local/payments.txt", Config.LocalStorage.Payments.format.replaceAll("<date>", LocalDate.now().format(DateTimeFormatter.ISO_DATE)).replaceAll("<count>", String.valueOf(getResult.getAmount())).replaceAll("<nickname>", getResult.getNickname()).replaceAll("<product_name>", getResult.getName()).replaceAll("<price>", String.valueOf(getResult.getPrice())).replaceAll("<payment_id>", String.valueOf(getResult.getId())));
    }

    public void addStatistic(GetResult getResult) {
    }
}
