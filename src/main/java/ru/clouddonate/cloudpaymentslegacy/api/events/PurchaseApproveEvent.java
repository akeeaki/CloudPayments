package ru.clouddonate.cloudpaymentslegacy.api.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ru.clouddonate.cloudpaymentslegacy.http.GetResult;

@Getter @Setter
public final class PurchaseApproveEvent extends Event {
    public static HandlerList handlerList = new HandlerList();
    private final GetResult result;

    public PurchaseApproveEvent(final GetResult result) {
        this.result = result;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
