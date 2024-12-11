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
import com.nexomc.nexo.api.events.furniture.NexoFurnitureDamageEvent;
import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockBreakEvent;
import com.nexomc.nexo.api.events.custom_block.stringblock.NexoStringBlockBreakEvent;
import com.nexomc.nexo.api.events.custom_block.stringblock.NexoStringBlockPlaceEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("On Custom Note Block Break")
@Description({"Fires when a Nexo note block gets broken."})
@Examples({"on break of custom note block:"})
@Since("1.0")
public class EvtNoteBlockBreakEvent extends SkriptEvent {

    private Literal<String> noteBlockID;

    static {
        Skript.registerEvent("Custom Note Block Break", EvtNoteBlockBreakEvent.class, NexoNoteBlockBreakEvent.class, "break of (custom|Nexo) (music|note) block [%string%]");
        EventValues.registerEventValue(NexoNoteBlockBreakEvent.class, Player.class, new Getter<Player, NexoNoteBlockBreakEvent>() {
            @Override
            public Player get(NexoNoteBlockBreakEvent arg) {
                return arg.getPlayer();
            }
        }, 0);
        EventValues.registerEventValue(NexoNoteBlockBreakEvent.class, Block.class, new Getter<Block, NexoNoteBlockBreakEvent>() {
            @Override
            public Block get(NexoNoteBlockBreakEvent arg) {
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
        if (e instanceof NexoNoteBlockBreakEvent) {
            NexoNoteBlockBreakEvent event = (NexoNoteBlockBreakEvent) e;
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
        return "custom note block break" + (noteBlockID != null ? noteBlockID.toString(e, debug) : "");
    }
}
