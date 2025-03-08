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
import com.nexomc.nexo.api.events.custom_block.stringblock.NexoStringBlockPlaceEvent;
import me.asleepp.skriptnexo.SkriptNexo;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Name("On Custom String Block Place")
@Description({"Fires when a Nexo string block gets placed."})
@Examples({"on place of custom string block:"})
@Since("1.0")
public class EvtStringBlockPlaceEvent extends SkriptEvent {

    private Literal<String> stringBlockID;
    private final Map<Player, Long> lastEventTimestamps = new HashMap<>();

    static {
        Skript.registerEvent("String Block Place", EvtStringBlockPlaceEvent.class, NexoStringBlockPlaceEvent.class, "place of (custom|Nexo) string block [%string%]");
        EventValues.registerEventValue(NexoStringBlockPlaceEvent.class, Player.class, new Converter<NexoStringBlockPlaceEvent, Player>() {
            @Override
            public Player convert(NexoStringBlockPlaceEvent arg) {
                return arg.getPlayer();
            }
        }, 0);
        EventValues.registerEventValue(NexoStringBlockPlaceEvent.class, Block.class, new Converter<NexoStringBlockPlaceEvent, Block>() {
            @Override
            public Block convert(NexoStringBlockPlaceEvent arg) {
                return arg.getBlock();
            }
        }, 0);
        EventValues.registerEventValue(NexoStringBlockPlaceEvent.class, ItemStack.class, new Converter<NexoStringBlockPlaceEvent, ItemStack>() {
            @Override
            public ItemStack convert(NexoStringBlockPlaceEvent arg) {
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
        if (e instanceof NexoStringBlockPlaceEvent) {
            NexoStringBlockPlaceEvent event = (NexoStringBlockPlaceEvent) e;

            if (!SkriptNexo.getInstance().getConfiguration().isEventEnabled("stringblock", "place")) {
                return false;
            }

            Player player = event.getPlayer();
            long currentTick = Bukkit.getCurrentTick();
            Long lastProcessedTick = lastEventTimestamps.get(player);

            int configCooldown = SkriptNexo.getInstance().getConfiguration().getEventCooldown("stringblock", "place");

            if (lastProcessedTick != null && (currentTick - lastProcessedTick) < configCooldown) {
                return false;
            }

            lastEventTimestamps.put(player, currentTick);

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