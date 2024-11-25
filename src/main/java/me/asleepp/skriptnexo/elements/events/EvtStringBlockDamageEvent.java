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
import io.th0rgal.oraxen.api.events.stringblock.OraxenStringBlockDamageEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("On Custom String Block Damage")
@Description({"Fires when a Oraxen string block gets damaged."})
@Examples({"on damage of custom string block:"})
@Since("1.0")
public class EvtStringBlockDamageEvent extends SkriptEvent {

    private Literal<String> stringBlockID;

    static {
        Skript.registerEvent("String Block Damage", EvtStringBlockDamageEvent.class, OraxenStringBlockDamageEvent.class, "damage of (custom|oraxen) string block [%string%]");
        EventValues.registerEventValue(OraxenStringBlockDamageEvent.class, Player.class, new Getter<Player, OraxenStringBlockDamageEvent>() {
            @Override
            public Player get(OraxenStringBlockDamageEvent arg) {
                return arg.getPlayer();
            }
        }, 0);
        EventValues.registerEventValue(OraxenStringBlockDamageEvent.class, Block.class, new Getter<Block, OraxenStringBlockDamageEvent>() {
            @Override
            public Block get(OraxenStringBlockDamageEvent arg) {
                return arg.getBlock();
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
        if (e instanceof OraxenStringBlockDamageEvent) {
            OraxenStringBlockDamageEvent event = (OraxenStringBlockDamageEvent) e;
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
        return "custom string block damage" + (stringBlockID != null ? stringBlockID.toString(e, debug) : "");
    }
}
