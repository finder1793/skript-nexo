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
import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockDamageEvent;
import me.asleepp.skriptnexo.SkriptNexo;
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

    @SuppressWarnings("unchecked")
    private static void register() {
        SyntaxRegistry syntaxRegistry = SkriptNexo.getAddonInstance().syntaxRegistry();
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY,
            BukkitSyntaxInfos.Event.builder(EvtNoteBlockDamageEvent.class, "Custom Note Block Damage")
                .addEvent(NexoNoteBlockDamageEvent.class)
                .addPatterns("damage of (custom|Nexo) (music|note) block [%string%]")
                .supplier(EvtNoteBlockDamageEvent::new)
                .build());
        EventValueRegistry evr = SkriptNexo.getAddonInstance().registry(EventValueRegistry.class);
        evr.register(EventValue.simple(NexoNoteBlockDamageEvent.class, Player.class, NexoNoteBlockDamageEvent::getPlayer));
        evr.register(EventValue.simple(NexoNoteBlockDamageEvent.class, Block.class, NexoNoteBlockDamageEvent::getBlock));
    }

    static { register(); }

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
