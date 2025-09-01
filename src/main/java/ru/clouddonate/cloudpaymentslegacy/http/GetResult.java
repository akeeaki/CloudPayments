package ru.clouddonate.cloudpaymentslegacy.http;

public class GetResult {
    private final int id;
    private final int product_id;
    private final String name;
    private final int price;
    private final int amount;
    private final String nickname;
    private final String[] commands;

    public int getId() {
        return this.id;
    }

    public int getProduct_id() {
        return this.product_id;
    }

    public String getName() {
        return this.name;
    }

    public int getAmount() {
        return this.amount;
    }

    public int getPrice() {
        return this.price;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String[] getCommands() {
        return this.commands;
    }

    public GetResult(int id, int product_id, String name, int price, String nickname, String[] commands, int amount) {
        this.id = id;
        this.product_id = product_id;
        this.name = name;
        this.price = price;
        this.nickname = nickname;
        this.commands = commands;
        this.amount = amount;
    }
}
