package me.asleepp.skriptnexo.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import io.th0rgal.oraxen.api.events.furniture.OraxenFurnitureInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
@Name("On Custom Furniture Interact")
@Description({"Fires when an Oraxen furniture gets interacted with."})
@Examples({"on interact with custom furniture:"})
@Since("1.0")
public class EvtFurnitureInteractEvent extends SkriptEvent {
    private Literal<String> furnitureID;

    static {
        Skript.registerEvent("Furniture Break", EvtFurnitureInteractEvent.class, OraxenFurnitureInteractEvent.class, "interact with (custom|oraxen) furniture [%string%]");
        EventValues.registerEventValue(OraxenFurnitureInteractEvent.class, Player.class, new Getter<Player, OraxenFurnitureInteractEvent>() {
            @Override
            public Player get(OraxenFurnitureInteractEvent arg) {
                return arg.getPlayer();
            }
        }, 0);
        EventValues.registerEventValue(OraxenFurnitureInteractEvent.class, ItemStack.class, new Getter<ItemStack, OraxenFurnitureInteractEvent>() {
            @Override
            public ItemStack get(OraxenFurnitureInteractEvent arg) {
                return arg.getItemInHand();
            }
        }, 0);
    }

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        furnitureID = (Literal<String>) args[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (e instanceof OraxenFurnitureInteractEvent) {
            OraxenFurnitureInteractEvent event = (OraxenFurnitureInteractEvent) e;
            if (furnitureID == null) {
                return !event.isCancelled();
            } else {
                String id = event.getMechanic().getItemID();
                if (id.equals(furnitureID.getSingle(e))) {
                    return !event.isCancelled();
                }
            }
        }
        return false;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "custom furniture " + (furnitureID != null ? furnitureID.toString(e, debug) : "");
    }
}
