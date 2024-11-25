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
import io.th0rgal.oraxen.api.events.noteblock.OraxenNoteBlockBreakEvent;
import io.th0rgal.oraxen.api.events.noteblock.OraxenNoteBlockPlaceEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("On Custom Note Block Place")
@Description({"Fires when a Oraxen note block gets placed."})
@Examples({"on place of custom note block:"})
@Since("1.0")
public class EvtNoteBlockPlaceEvent extends SkriptEvent {

    private Literal<String> noteBlockID;

    static {
        Skript.registerEvent("Custom Note Block Place", EvtNoteBlockPlaceEvent.class, OraxenNoteBlockPlaceEvent.class, "place of (custom|oraxen) (music|note) block [%string%]");
        EventValues.registerEventValue(OraxenNoteBlockPlaceEvent.class, Player.class, new Getter<Player, OraxenNoteBlockPlaceEvent>() {
            @Override
            public @Nullable Player get(OraxenNoteBlockPlaceEvent arg) {
                return arg.getPlayer();
            }
        }, 0);
        EventValues.registerEventValue(OraxenNoteBlockPlaceEvent.class, Block.class, new Getter<Block, OraxenNoteBlockPlaceEvent>() {
            @Override
            public Block get(OraxenNoteBlockPlaceEvent arg) {
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
        if (e instanceof OraxenNoteBlockPlaceEvent) {
            OraxenNoteBlockPlaceEvent event = (OraxenNoteBlockPlaceEvent) e;
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
