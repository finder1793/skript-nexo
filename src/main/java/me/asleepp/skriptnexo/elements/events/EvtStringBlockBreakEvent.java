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
import io.th0rgal.oraxen.api.events.noteblock.OraxenNoteBlockInteractEvent;
import io.th0rgal.oraxen.api.events.stringblock.OraxenStringBlockBreakEvent;
import io.th0rgal.oraxen.api.events.stringblock.OraxenStringBlockInteractEvent;
import io.th0rgal.oraxen.utils.drops.Drop;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("On Custom String Block Break")
@Description({"Fires when a Oraxen string block gets broken."})
@Examples({"on break of custom string block:"})
@Since("1.0")
public class EvtStringBlockBreakEvent extends SkriptEvent {

    private Literal<String> stringBlockID;

    static {
        Skript.registerEvent("String Block Break", EvtStringBlockBreakEvent.class, OraxenStringBlockBreakEvent.class, "break of (custom|oraxen) string block [%string%]");
        EventValues.registerEventValue(OraxenStringBlockBreakEvent.class, Player.class, new Getter<Player, OraxenStringBlockBreakEvent>() {
            @Override
            public Player get(OraxenStringBlockBreakEvent arg) {
                return arg.getPlayer();
            }
        },0);
        EventValues.registerEventValue(OraxenStringBlockBreakEvent.class, Block.class, new Getter<Block, OraxenStringBlockBreakEvent>() {
            @Override
            public Block get(OraxenStringBlockBreakEvent arg) {
                return arg.getBlock();
            }
        },0);
        EventValues.registerEventValue(OraxenStringBlockBreakEvent.class, Drop.class, new Getter<Drop, OraxenStringBlockBreakEvent>() {
            @Override
            public Drop get(OraxenStringBlockBreakEvent arg) {
                return arg.getDrop();
            }
        },0);
    }

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        stringBlockID = (Literal<String>) args[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (e instanceof OraxenStringBlockBreakEvent) {
            OraxenStringBlockBreakEvent event = (OraxenStringBlockBreakEvent) e;
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
