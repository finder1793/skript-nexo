package me.asleepp.skriptnexo.mechanics.events;

import me.asleepp.skriptnexo.mechanics.SkriptMechanic;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public class SkriptMechanicInteractEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final SkriptMechanic mechanic;
    private final Action action;
    private final ItemStack item;
    private boolean cancelled = false;
    
    public SkriptMechanicInteractEvent(Player player, SkriptMechanic mechanic, Action action, ItemStack item) {
        this.player = player;
        this.mechanic = mechanic;
        this.action = action;
        this.item = item;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public SkriptMechanic getMechanic() {
        return mechanic;
    }
    
    public Action getAction() {
        return action;
    }
    
    public ItemStack getItem() {
        return item;
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled;
    }
    
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
}