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
import com.nexomc.nexo.api.events.custom_block.stringblock.NexoStringBlockBreakEvent;
import com.nexomc.nexo.utils.drops.Drop;
import me.asleepp.skriptnexo.SkriptNexo;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Name("On Custom String Block Break")
@Description({"Fires when a Nexo string block gets broken."})
@Examples({"on break of custom string block:"})
@Since("1.0")
public class EvtStringBlockBreakEvent extends SkriptEvent {

    private Literal<String> stringBlockID;
    private final Map<Player, Long> lastEventTimestamps = new HashMap<>();

    @SuppressWarnings("unchecked")
    private static void register() {
        SyntaxRegistry syntaxRegistry = SkriptNexo.getAddonInstance().syntaxRegistry();
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY,
            BukkitSyntaxInfos.Event.builder(EvtStringBlockBreakEvent.class, "String Block Break")
                .addEvent(NexoStringBlockBreakEvent.class)
                .addPatterns("break of (custom|Nexo) string block [%string%]")
                .supplier(EvtStringBlockBreakEvent::new)
                .build());
        EventValueRegistry evr = SkriptNexo.getAddonInstance().registry(EventValueRegistry.class);
        evr.register(EventValue.simple(NexoStringBlockBreakEvent.class, Player.class, NexoStringBlockBreakEvent::getPlayer));
        evr.register(EventValue.simple(NexoStringBlockBreakEvent.class, Block.class, NexoStringBlockBreakEvent::getBlock));
        evr.register(EventValue.simple(NexoStringBlockBreakEvent.class, Drop.class, NexoStringBlockBreakEvent::getDrop));
    }

    static { register(); }

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        stringBlockID = (Literal<String>) args[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (e instanceof NexoStringBlockBreakEvent) {
            NexoStringBlockBreakEvent event = (NexoStringBlockBreakEvent) e;

            if (!SkriptNexo.getInstance().getConfiguration().isEventEnabled("stringblock", "break")) {
                return false;
            }

            Player player = event.getPlayer();
            long currentTick = Bukkit.getCurrentTick();
            Long lastProcessedTick = lastEventTimestamps.get(player);

            int configCooldown = SkriptNexo.getInstance().getConfiguration().getEventCooldown("stringblock", "break");

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
        return "custom string block break" + (stringBlockID != null ? stringBlockID.toString(e, debug) : "");
    }
}