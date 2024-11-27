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
import com.nexomc.nexo.api.events.noteblock.NexoNoteBlockBreakEvent;
import com.nexomc.nexo.api.events.noteblock.NexoNoteBlockInteractEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("On Custom Note Block Interact")
@Description({"Fires when a Nexo note block gets interacted with."})
@Examples({"on interact with custom note block:"})
@Since("1.0")
public class EvtNoteBlockInteractEvent extends SkriptEvent {

    private Literal<String> noteBlockID;

    static {
        Skript.registerEvent("Custom Note Block Interact", EvtNoteBlockInteractEvent.class, NexoNoteBlockInteractEvent.class, "interact with (custom|Nexo) (music|note) block [%string%]");
        EventValues.registerEventValue(NexoNoteBlockInteractEvent.class, Player.class, new Getter<Player, NexoNoteBlockInteractEvent>() {
            @Override
            public Player get(NexoNoteBlockInteractEvent arg) {
                return arg.getPlayer();
            }
        }, 0);
        EventValues.registerEventValue(NexoNoteBlockInteractEvent.class, Block.class, new Getter<Block, NexoNoteBlockInteractEvent>() {
            @Override
            public Block get(NexoNoteBlockInteractEvent arg) {
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
        if (e instanceof NexoNoteBlockInteractEvent) {
            NexoNoteBlockInteractEvent event = (NexoNoteBlockInteractEvent) e;
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
