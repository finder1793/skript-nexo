package me.asleepp.skriptnexo.elements.events;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;
import com.nexomc.nexo.api.events.furniture.NexoFurniturePlaceEvent;
import me.asleepp.skriptnexo.SkriptNexo;
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

    @SuppressWarnings("unchecked")
    private static void register() {
        SyntaxRegistry syntaxRegistry = SkriptNexo.getAddonInstance().syntaxRegistry();
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY,
            BukkitSyntaxInfos.Event.builder(EvtFurniturePlaceEvent.class, "Furniture Place")
                .addEvent(NexoFurniturePlaceEvent.class)
                .addPatterns("place of (custom|Nexo) furniture [%string%]")
                .supplier(EvtFurniturePlaceEvent::new)
                .build());
        EventValueRegistry evr = SkriptNexo.getAddonInstance().registry(EventValueRegistry.class);
        evr.register(EventValue.simple(NexoFurniturePlaceEvent.class, Player.class, NexoFurniturePlaceEvent::getPlayer));
        evr.register(EventValue.simple(NexoFurniturePlaceEvent.class, ItemStack.class, NexoFurniturePlaceEvent::getItemInHand));
        evr.register(EventValue.simple(NexoFurniturePlaceEvent.class, Location.class, e -> e.getBaseEntity().getLocation()));
    }

    static { register(); }

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
