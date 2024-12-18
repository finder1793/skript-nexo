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
import com.nexomc.nexo.api.events.furniture.NexoFurnitureBreakEvent;
import me.asleepp.skriptnexo.SkriptNexo;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.Location;
import org.bukkit.Bukkit;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Name("On Custom Furniture Break")
@Description({"Fires when an Nexo furniture gets broken."})
@Examples({"on break of custom furniture:"})
@Since("1.0")
public class EvtFurnitureBreakEvent extends SkriptEvent {
    private Literal<String> furnitureID;
    private final Map<Player, Long> lastEventTimestamps = new HashMap<>();

    static {
        Skript.registerEvent("Furniture Break", EvtFurnitureBreakEvent.class, NexoFurnitureBreakEvent.class, "break of (custom|Nexo) furniture [%string%]");
        EventValues.registerEventValue(NexoFurnitureBreakEvent.class, Player.class, new Getter<Player, NexoFurnitureBreakEvent>() {
            @Override
            public Player get(NexoFurnitureBreakEvent arg) {
                return arg.getPlayer();
            }
        }, 0);
        EventValues.registerEventValue(NexoFurnitureBreakEvent.class, Location.class, new Getter<Location, NexoFurnitureBreakEvent>() {
            @Override
            public Location get(NexoFurnitureBreakEvent event) {
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
        if (e instanceof NexoFurnitureBreakEvent) {
            NexoFurnitureBreakEvent event = (NexoFurnitureBreakEvent) e;

            // Check if event is enabled in config
            if (!SkriptNexo.getInstance().getConfiguration().isEventEnabled("furniture", "break")) {
                return false;
            }

            Player player = event.getPlayer();
            long currentTick = Bukkit.getCurrentTick();
            Long lastProcessedTick = lastEventTimestamps.get(player);

            int configCooldown = SkriptNexo.getInstance().getConfiguration().getEventCooldown("furniture", "break");

            if (lastProcessedTick != null && (currentTick - lastProcessedTick) < configCooldown) {
                return false;
            }

            lastEventTimestamps.put(player, currentTick);

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