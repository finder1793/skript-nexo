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
import com.nexomc.nexo.api.events.furniture.NexoFurnitureInteractEvent;
import me.asleepp.skriptnexo.SkriptNexo;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.Bukkit;
import org.bukkit.inventory.EquipmentSlot;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Name("On Custom Furniture Interact")
@Description({"Fires when an Nexo furniture gets interacted with."})
@Examples({"on interact with (custom|nexo) furniture:"})
@Since("1.0")
public class EvtFurnitureInteractEvent extends SkriptEvent {
    private Literal<String> furnitureID;
    private final Map<Player, Long> lastEventTimestamps = new HashMap<>();

    static {
        Skript.registerEvent("Furniture interact", EvtFurnitureInteractEvent.class, NexoFurnitureInteractEvent.class, "interact with (custom|Nexo) furniture [%string%]");
        EventValues.registerEventValue(NexoFurnitureInteractEvent.class, Player.class, new Getter<Player, NexoFurnitureInteractEvent>() {
            @Override
            public Player get(NexoFurnitureInteractEvent arg) {
                return arg.getPlayer();
            }
        }, 0);
        EventValues.registerEventValue(NexoFurnitureInteractEvent.class, ItemStack.class, new Getter<ItemStack, NexoFurnitureInteractEvent>() {
            @Override
            public ItemStack get(NexoFurnitureInteractEvent arg) {
                return arg.getItemInHand();
            }
        }, 0);
        EventValues.registerEventValue(NexoFurnitureInteractEvent.class, Location.class, new Getter<Location, NexoFurnitureInteractEvent>() {
            @Override
            public Location get(NexoFurnitureInteractEvent event) {
                return event.getBaseEntity().getLocation();
            }
        }, 0);
        EventValues.registerEventValue(NexoFurnitureInteractEvent.class, BlockFace.class, new Getter<BlockFace, NexoFurnitureInteractEvent>() {
            @Override
            public BlockFace get(NexoFurnitureInteractEvent event) {
                return event.getBlockFace();
            }
        }, 0);
        EventValues.registerEventValue(NexoFurnitureInteractEvent.class, EquipmentSlot.class, new Getter<EquipmentSlot, NexoFurnitureInteractEvent>() {
            @Override
            public EquipmentSlot get(NexoFurnitureInteractEvent event) {
                return event.getHand();
            }
        }, 0);
    }

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        if (args.length > 0) {
            furnitureID = (Literal<String>) args[0];
        }
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (e instanceof NexoFurnitureInteractEvent) {
            NexoFurnitureInteractEvent event = (NexoFurnitureInteractEvent) e;
            Player player = event.getPlayer();

            // If player is not sneaking, cancel block placement
            if (!player.isSneaking()) {
                event.setCancelled(true);
            } else {
                // Player is sneaking, let them place blocks
                return false;
            }

            if (!SkriptNexo.getInstance().getConfiguration().isEventEnabled("furniture", "interact")) {
                return false;
            }

            long currentTick = Bukkit.getCurrentTick();
            Long lastProcessedTick = lastEventTimestamps.get(player);

            int configCooldown = SkriptNexo.getInstance().getConfiguration().getEventCooldown("furniture", "interact");

            if (lastProcessedTick != null && (currentTick - lastProcessedTick) < configCooldown) {
                return false;
            }

            lastEventTimestamps.put(player, currentTick);

            if (furnitureID == null) {
                return !event.isCancelled();
            } else {
                String id = event.getMechanic().getItemID();
                if (id.equals(furnitureID.getSingle(e))) {
                    return !event.isCancelled();
                }
            }
        }
        return false;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "custom furniture " + (furnitureID != null ? furnitureID.toString(e, debug) : "");
    }
}