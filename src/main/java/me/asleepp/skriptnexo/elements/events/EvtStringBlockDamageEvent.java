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
import com.nexomc.nexo.api.events.custom_block.stringblock.NexoStringBlockDamageEvent;
import me.asleepp.skriptnexo.SkriptNexo;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("On Custom String Block Damage")
@Description({"Fires when a Nexo string block gets damaged."})
@Examples({"on damage of custom string block:"})
@Since("1.0")
public class EvtStringBlockDamageEvent extends SkriptEvent {

    private Literal<String> stringBlockID;

    @SuppressWarnings("unchecked")
    private static void register() {
        SyntaxRegistry syntaxRegistry = SkriptNexo.getAddonInstance().syntaxRegistry();
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY,
            BukkitSyntaxInfos.Event.builder(EvtStringBlockDamageEvent.class, "String Block Damage")
                .addEvent(NexoStringBlockDamageEvent.class)
                .addPatterns("damage of (custom|Nexo) string block [%string%]")
                .supplier(EvtStringBlockDamageEvent::new)
                .build());
        EventValueRegistry evr = SkriptNexo.getAddonInstance().registry(EventValueRegistry.class);
        evr.register(EventValue.simple(NexoStringBlockDamageEvent.class, Player.class, NexoStringBlockDamageEvent::getPlayer));
        evr.register(EventValue.simple(NexoStringBlockDamageEvent.class, Block.class, NexoStringBlockDamageEvent::getBlock));
    }

    static { register(); }
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        stringBlockID = (Literal<String>) args[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (e instanceof NexoStringBlockDamageEvent) {
            NexoStringBlockDamageEvent event = (NexoStringBlockDamageEvent) e;
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
