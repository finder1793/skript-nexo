package me.asleepp.skriptnexo.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;
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

    @SuppressWarnings("unchecked")
    private static void register() {
        SyntaxRegistry syntaxRegistry = SkriptNexo.getAddonInstance().syntaxRegistry();
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY,
            BukkitSyntaxInfos.Event.builder(EvtMechanicInteractEvent.class, "Mechanic Interact")
                .addEvent(SkriptMechanicInteractEvent.class)
                .addPatterns("interact with [mechanic] %string%")
                .supplier(EvtMechanicInteractEvent::new)
                .build());
        EventValueRegistry evr = SkriptNexo.getAddonInstance().registry(EventValueRegistry.class);
        evr.register(EventValue.simple(SkriptMechanicInteractEvent.class, Player.class, SkriptMechanicInteractEvent::getPlayer));
        evr.register(EventValue.simple(SkriptMechanicInteractEvent.class, ItemStack.class, SkriptMechanicInteractEvent::getItem));
        evr.register(EventValue.simple(SkriptMechanicInteractEvent.class, Location.class, e -> e.getPlayer().getLocation()));
        evr.register(EventValue.simple(SkriptMechanicInteractEvent.class, Action.class, SkriptMechanicInteractEvent::getAction));
    }

    static { register(); }

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
