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
import org.skriptlang.skript.lang.converter.Converter;
import com.nexomc.nexo.api.events.furniture.NexoFurniturePlaceEvent;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
@Name("On Custom Furniture Place")
@Description({"Fires when an Nexo furniture gets placed."})
@Examples({"on place of custom furniture:"})
@Since("1.0")
public class EvtFurniturePlaceEvent extends SkriptEvent {
    private Literal<String> furnitureID;

    static {
        Skript.registerEvent("Furniture Place", EvtFurniturePlaceEvent.class, NexoFurniturePlaceEvent.class, "place of (custom|Nexo) furniture [%string%]");
        EventValues.registerEventValue(NexoFurniturePlaceEvent.class, Player.class, new Converter<NexoFurniturePlaceEvent, Player>() {
            @Override
            public Player convert(NexoFurniturePlaceEvent arg) {
                return arg.getPlayer();
            }
        }, 0);
        EventValues.registerEventValue(NexoFurniturePlaceEvent.class, ItemStack.class, new Converter<NexoFurniturePlaceEvent, ItemStack>() {
            @Override
            public ItemStack convert(NexoFurniturePlaceEvent arg) {
                return arg.getItemInHand();
            }
        }, 0);
        EventValues.registerEventValue(NexoFurniturePlaceEvent.class, Location.class, new Converter<NexoFurniturePlaceEvent, Location>() {
            @Override
            public Location convert(NexoFurniturePlaceEvent event) {
                return event.getBaseEntity().getLocation();
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
        if (e instanceof NexoFurniturePlaceEvent) {
            NexoFurniturePlaceEvent event = (NexoFurniturePlaceEvent) e;
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
