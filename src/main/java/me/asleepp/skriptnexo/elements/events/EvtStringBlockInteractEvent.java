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
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("On Custom String Block Interact")
@Description({"Fires when a Oraxen string block gets interacted with."})
@Examples({"on interact with custom string block:"})
@Since("1.0")
public class EvtStringBlockInteractEvent extends SkriptEvent {

    private Literal<String> stringBlockID;

    static {
        Skript.registerEvent("String Block Interact", EvtStringBlockInteractEvent.class, OraxenStringBlockInteractEvent.class, "interact with (custom|oraxen) string block [%string%]");
        EventValues.registerEventValue(OraxenStringBlockInteractEvent.class, Player.class, new Getter<Player, OraxenStringBlockInteractEvent>() {
            @Override
            public Player get(OraxenStringBlockInteractEvent arg) {
                return arg.getPlayer();
            }
        }, 0);
        EventValues.registerEventValue(OraxenStringBlockInteractEvent.class, Block.class, new Getter<Block, OraxenStringBlockInteractEvent>() {
            @Override
            public Block get(OraxenStringBlockInteractEvent arg) {
                return arg.getBlock();
            }
        }, 0);
        EventValues.registerEventValue(OraxenStringBlockInteractEvent.class, BlockFace.class, new Getter<BlockFace, OraxenStringBlockInteractEvent>() {
            @Override
            public BlockFace get(OraxenStringBlockInteractEvent arg) {
                return arg.getBlockFace();
            }
        }, 0);

    }    
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        stringBlockID = (Literal<String>) args[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (e instanceof OraxenStringBlockInteractEvent) {
            OraxenStringBlockInteractEvent event = (OraxenStringBlockInteractEvent) e;
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
        return "custom string block interact" + (stringBlockID != null ? stringBlockID.toString(e, debug) : "");
    }
}
