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
import io.th0rgal.oraxen.api.events.stringblock.OraxenStringBlockPlaceEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
@Name("On Custom String Block Place")
@Description({"Fires when a Oraxen string block gets placed."})
@Examples({"on place of custom string block:"})
@Since("1.0")
public class EvtStringBlockPlaceEvent extends SkriptEvent {

    private Literal<String> stringBlockID;

    static {
        Skript.registerEvent("String Block Place", EvtStringBlockPlaceEvent.class, OraxenStringBlockPlaceEvent.class, "break of (custom|oraxen) string block [%string%]");
        EventValues.registerEventValue(OraxenStringBlockPlaceEvent.class, Player.class, new Getter<Player, OraxenStringBlockPlaceEvent>() {
            @Override
            public Player get(OraxenStringBlockPlaceEvent arg) {
                return arg.getPlayer();
            }
        }, 0);
        EventValues.registerEventValue(OraxenStringBlockPlaceEvent.class, Block.class, new Getter<Block, OraxenStringBlockPlaceEvent>() {
            @Override
            public Block get(OraxenStringBlockPlaceEvent arg) {
                return arg.getBlock();
            }
        }, 0);
        EventValues.registerEventValue(OraxenStringBlockPlaceEvent.class, ItemStack.class, new Getter<ItemStack, OraxenStringBlockPlaceEvent>() {
            @Override
            public ItemStack get(OraxenStringBlockPlaceEvent arg) {
                return arg.getItemInHand();
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
        if (e instanceof OraxenStringBlockPlaceEvent) {
            OraxenStringBlockPlaceEvent event = (OraxenStringBlockPlaceEvent) e;
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
        return "custom string block place" + (stringBlockID != null ? stringBlockID.toString(e, debug) : "");
    }
}
