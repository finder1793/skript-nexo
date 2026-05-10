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
import com.nexomc.nexo.api.events.custom_block.stringblock.NexoStringBlockPlaceEvent;
import me.asleepp.skriptnexo.SkriptNexo;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Name("On Custom String Block Place")
@Description({"Fires when a Nexo string block gets placed."})
@Examples({"on place of custom string block:"})
@Since("1.0")
public class EvtStringBlockPlaceEvent extends SkriptEvent {

    private Literal<String> stringBlockID;
    private final Map<Player, Long> lastEventTimestamps = new HashMap<>();

    @SuppressWarnings("unchecked")
    private static void register() {
        SyntaxRegistry syntaxRegistry = SkriptNexo.getAddonInstance().syntaxRegistry();
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY,
            BukkitSyntaxInfos.Event.builder(EvtStringBlockPlaceEvent.class, "String Block Place")
                .addEvent(NexoStringBlockPlaceEvent.class)
                .addPatterns("place of (custom|Nexo) string block [%string%]")
                .supplier(EvtStringBlockPlaceEvent::new)
                .build());
        EventValueRegistry evr = SkriptNexo.getAddonInstance().registry(EventValueRegistry.class);
        evr.register(EventValue.simple(NexoStringBlockPlaceEvent.class, Player.class, NexoStringBlockPlaceEvent::getPlayer));
        evr.register(EventValue.simple(NexoStringBlockPlaceEvent.class, Block.class, NexoStringBlockPlaceEvent::getBlock));
        evr.register(EventValue.simple(NexoStringBlockPlaceEvent.class, ItemStack.class, NexoStringBlockPlaceEvent::getItemInHand));
    }

    static { register(); }

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        stringBlockID = (Literal<String>) args[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (e instanceof NexoStringBlockPlaceEvent) {
            NexoStringBlockPlaceEvent event = (NexoStringBlockPlaceEvent) e;

            if (!SkriptNexo.getInstance().getConfiguration().isEventEnabled("stringblock", "place")) {
                return false;
            }

            Player player = event.getPlayer();
            long currentTick = Bukkit.getCurrentTick();
            Long lastProcessedTick = lastEventTimestamps.get(player);

            int configCooldown = SkriptNexo.getInstance().getConfiguration().getEventCooldown("stringblock", "place");

            if (lastProcessedTick != null && (currentTick - lastProcessedTick) < configCooldown) {
                return false;
            }

            lastEventTimestamps.put(player, currentTick);

            if (stringBlockID == null) {
                return !event.isCancelled();
            } else {
                String id = event.getMechanic().getItemID();
                if (id.equals(stringBlockID.getSingle(e))) {
                    return !event.isCancelled();
                }
            }
        }
        return false;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "custom string block place" + (stringBlockID != null ? stringBlockID.toString(e, debug) : "");
    }
}