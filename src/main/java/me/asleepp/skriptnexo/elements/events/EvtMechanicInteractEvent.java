package me.asleepp.skriptnexo.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import org.skriptlang.skript.lang.converter.Converter;
import me.asleepp.skriptnexo.mechanics.events.SkriptMechanicInteractEvent;
import me.asleepp.skriptnexo.SkriptNexo;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.event.block.Action;
import org.bukkit.Bukkit;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class EvtMechanicInteractEvent extends SkriptEvent {
    private Literal<String> mechanicId;
    private final Map<Player, Long> lastEventTimestamps = new HashMap<>();

    static {
        Skript.registerEvent("Mechanic Interact", EvtMechanicInteractEvent.class, SkriptMechanicInteractEvent.class, 
            "interact with [mechanic] %string%");

        // Register event values
        EventValues.registerEventValue(SkriptMechanicInteractEvent.class, Player.class, new Converter<SkriptMechanicInteractEvent, Player>() {
            @Override
            public Player convert(SkriptMechanicInteractEvent event) {
                return event.getPlayer();
            }
        }, 0);

        EventValues.registerEventValue(SkriptMechanicInteractEvent.class, ItemStack.class, new Converter<SkriptMechanicInteractEvent, ItemStack>() {
            @Override
            public ItemStack convert(SkriptMechanicInteractEvent event) {
                return event.getItem();
            }
        }, 0);

        EventValues.registerEventValue(SkriptMechanicInteractEvent.class, Location.class, new Converter<SkriptMechanicInteractEvent, Location>() {
            @Override
            public Location convert(SkriptMechanicInteractEvent event) {
                return event.getPlayer().getLocation();
            }
        }, 0);

        EventValues.registerEventValue(SkriptMechanicInteractEvent.class, Action.class, new Converter<SkriptMechanicInteractEvent, Action>() {
            @Override
            public Action convert(SkriptMechanicInteractEvent event) {
                return event.getAction();
            }
        }, 0);
    }

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        mechanicId = (Literal<String>) args[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (e instanceof SkriptMechanicInteractEvent) {
            SkriptMechanicInteractEvent event = (SkriptMechanicInteractEvent) e;
            Player player = event.getPlayer();
            String id = event.getMechanic().getMechanicId();

            // Check if the event is enabled in the configuration
            if (!SkriptNexo.getInstance().getConfiguration().isEventEnabled("mechanic", "interact")) {
                return false;
            }

            // Apply cooldown
            long currentTick = Bukkit.getCurrentTick();
            Long lastProcessedTick = lastEventTimestamps.get(player);

            int configCooldown = SkriptNexo.getInstance().getConfiguration().getEventCooldown("mechanic", "interact");

            if (lastProcessedTick != null && (currentTick - lastProcessedTick) < configCooldown) {
                return false;
            }

            lastEventTimestamps.put(player, currentTick);

            // Check if the mechanic ID matches
            return id.equals(mechanicId.getSingle(e));
        }
        return false;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "interact with mechanic " + mechanicId.toString(e, debug);
    }
}
