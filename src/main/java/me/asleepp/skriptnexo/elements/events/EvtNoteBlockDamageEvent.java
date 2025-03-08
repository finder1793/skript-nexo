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
import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockBreakEvent;
import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockDamageEvent;
import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockPlaceEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("On Custom Note Block Damage")
@Description({"Fires when a Nexo note block gets damaged."})
@Examples({"on damage of custom note block:"})
@Since("1.0")
public class EvtNoteBlockDamageEvent extends SkriptEvent {

    private Literal<String> noteBlockID;

    static {
        Skript.registerEvent("Custom Note Block Damage", EvtFurnitureDamageEvent.class, NexoNoteBlockDamageEvent.class, "damage of (custom|Nexo) (music|note) block [%string%]");
        EventValues.registerEventValue(NexoNoteBlockDamageEvent.class, Player.class, new Converter<NexoNoteBlockDamageEvent, Player>() {
            @Override
            public Player convert(NexoNoteBlockDamageEvent arg) {
                return arg.getPlayer();
            }
        }, 0);
        EventValues.registerEventValue(NexoNoteBlockDamageEvent.class, Block.class, new Converter<NexoNoteBlockDamageEvent, Block>() {
            @Override
            public Block convert(NexoNoteBlockDamageEvent arg) {
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
        if (e instanceof NexoNoteBlockDamageEvent) {
            NexoNoteBlockDamageEvent event = (NexoNoteBlockDamageEvent) e;
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
