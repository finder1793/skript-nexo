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
import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockInteractEvent;
import com.nexomc.nexo.api.events.custom_block.stringblock.NexoStringBlockBreakEvent;
import com.nexomc.nexo.api.events.custom_block.stringblock.NexoStringBlockInteractEvent;
import com.nexomc.nexo.utils.drops.Drop;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("On Custom String Block Interact")
@Description({"Fires when a Nexo string block gets interacted with."})
@Examples({"on interact with custom string block:"})
@Since("1.0")
public class EvtStringBlockInteractEvent extends SkriptEvent {

    private Literal<String> stringBlockID;

    static {
        Skript.registerEvent("String Block Interact", EvtStringBlockInteractEvent.class, NexoStringBlockInteractEvent.class, "interact with (custom|Nexo) string block [%string%]");
        EventValues.registerEventValue(NexoStringBlockInteractEvent.class, Player.class, new Getter<Player, NexoStringBlockInteractEvent>() {
            @Override
            public Player get(NexoStringBlockInteractEvent arg) {
                return arg.getPlayer();
            }
        }, 0);
        EventValues.registerEventValue(NexoStringBlockInteractEvent.class, Block.class, new Getter<Block, NexoStringBlockInteractEvent>() {
            @Override
            public Block get(NexoStringBlockInteractEvent arg) {
                return arg.getBlock();
            }
        }, 0);
        EventValues.registerEventValue(NexoStringBlockInteractEvent.class, BlockFace.class, new Getter<BlockFace, NexoStringBlockInteractEvent>() {
            @Override
            public BlockFace get(NexoStringBlockInteractEvent arg) {
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
        if (e instanceof NexoStringBlockInteractEvent) {
            NexoStringBlockInteractEvent event = (NexoStringBlockInteractEvent) e;
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
