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
import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockPlaceEvent;
import me.asleepp.skriptnexo.SkriptNexo;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Name("On Custom Note Block Place")
@Description({"Fires when a Nexo note block gets placed."})
@Examples({"on place of custom note block:"})
@Since("1.0")
public class EvtNoteBlockPlaceEvent extends SkriptEvent {

    private Literal<String> noteBlockID;
    private final Map<Player, Long> lastEventTimestamps = new HashMap<>();

    static {
        Skript.registerEvent("Custom Note Block Place", EvtNoteBlockPlaceEvent.class, NexoNoteBlockPlaceEvent.class, "place of (custom|Nexo) (music|note) block [%string%]");
        EventValues.registerEventValue(NexoNoteBlockPlaceEvent.class, Player.class, new Converter<NexoNoteBlockPlaceEvent, Player>() {
            @Override
            public Player convert(NexoNoteBlockPlaceEvent arg) {
                return arg.getPlayer();
            }
        }, 0);
        EventValues.registerEventValue(NexoNoteBlockPlaceEvent.class, Block.class, new Converter<NexoNoteBlockPlaceEvent, Block>() {
            @Override
            public Block convert(NexoNoteBlockPlaceEvent arg) {
                return arg.getBlock();
            }
        }, 0);
    }

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        noteBlockID = (Literal<String>) args[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (e instanceof NexoNoteBlockPlaceEvent) {
            NexoNoteBlockPlaceEvent event = (NexoNoteBlockPlaceEvent) e;

            if (!SkriptNexo.getInstance().getConfiguration().isEventEnabled("noteblock", "place")) {
                return false;
            }

            Player player = event.getPlayer();
            long currentTick = Bukkit.getCurrentTick();
            Long lastProcessedTick = lastEventTimestamps.get(player);

            int configCooldown = SkriptNexo.getInstance().getConfiguration().getEventCooldown("noteblock", "place");

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
        return "custom note block place" + (noteBlockID != null ? noteBlockID.toString(e, debug) : "");
    }
}