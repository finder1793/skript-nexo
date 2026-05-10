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
import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockInteractEvent;
import me.asleepp.skriptnexo.SkriptNexo;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Name("On Custom Note Block Interact")
@Description({"Fires when a Nexo note block gets interacted with."})
@Examples({"on interact with custom note block:"})
@Since("1.0")
public class EvtNoteBlockInteractEvent extends SkriptEvent {

    private Literal<String> noteBlockID;
    private final Map<Player, Long> lastEventTimestamps = new HashMap<>();

    @SuppressWarnings("unchecked")
    private static void register() {
        SyntaxRegistry syntaxRegistry = SkriptNexo.getAddonInstance().syntaxRegistry();
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY,
            BukkitSyntaxInfos.Event.builder(EvtNoteBlockInteractEvent.class, "Custom Note Block Interact")
                .addEvent(NexoNoteBlockInteractEvent.class)
                .addPatterns("interact with (custom|Nexo) (music|note) block [%string%]")
                .supplier(EvtNoteBlockInteractEvent::new)
                .build());
        EventValueRegistry evr = SkriptNexo.getAddonInstance().registry(EventValueRegistry.class);
        evr.register(EventValue.simple(NexoNoteBlockInteractEvent.class, Player.class, NexoNoteBlockInteractEvent::getPlayer));
        evr.register(EventValue.simple(NexoNoteBlockInteractEvent.class, Block.class, NexoNoteBlockInteractEvent::getBlock));
    }

    static { register(); }

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        noteBlockID = (Literal<String>) args[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (e instanceof NexoNoteBlockInteractEvent) {
            NexoNoteBlockInteractEvent event = (NexoNoteBlockInteractEvent) e;
            Player player = event.getPlayer();

            if (!player.isSneaking()) {
                event.setCancelled(true);
            } else {
                return false;
            }

            if (!SkriptNexo.getInstance().getConfiguration().isEventEnabled("noteblock", "interact")) {
                return false;
            }

            long currentTick = Bukkit.getCurrentTick();
            Long lastProcessedTick = lastEventTimestamps.get(player);

            int configCooldown = SkriptNexo.getInstance().getConfiguration().getEventCooldown("noteblock", "interact");

            if (lastProcessedTick != null && (currentTick - lastProcessedTick) < configCooldown) {
                return false;
            }

            lastEventTimestamps.put(player, currentTick);

            if (noteBlockID == null) {
                return !event.isCancelled();
            } else {
                String id = event.getMechanic().getItemID();
                if (id.equals(noteBlockID.getSingle(e))) {
                    return !event.isCancelled();
                }
            }
        }
        return false;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "custom note block interact" + (noteBlockID != null ? noteBlockID.toString(e, debug) : "");
    }
}