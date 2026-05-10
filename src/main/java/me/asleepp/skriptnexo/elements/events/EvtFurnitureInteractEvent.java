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
    private static boolean skriptReflectPresent = false;

    @SuppressWarnings("unchecked")
    private static void register(String name, String pattern) {
        SyntaxRegistry syntaxRegistry = SkriptNexo.getAddonInstance().syntaxRegistry();
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY,
            BukkitSyntaxInfos.Event.builder(EvtFurnitureInteractEvent.class, name)
                .addEvent(NexoFurnitureInteractEvent.class)
                .addPatterns(pattern)
                .supplier(EvtFurnitureInteractEvent::new)
                .build());
        EventValueRegistry evr = SkriptNexo.getAddonInstance().registry(EventValueRegistry.class);
        evr.register(EventValue.simple(NexoFurnitureInteractEvent.class, Player.class, NexoFurnitureInteractEvent::getPlayer));
        evr.register(EventValue.simple(NexoFurnitureInteractEvent.class, ItemStack.class, NexoFurnitureInteractEvent::getItemInHand));
        evr.register(EventValue.simple(NexoFurnitureInteractEvent.class, Location.class, e -> e.getBaseEntity().getLocation()));
        evr.register(EventValue.simple(NexoFurnitureInteractEvent.class, BlockFace.class, NexoFurnitureInteractEvent::getBlockFace));
        evr.register(EventValue.simple(NexoFurnitureInteractEvent.class, EquipmentSlot.class, NexoFurnitureInteractEvent::getHand));
    }

    static {
        try {
            Class.forName("com.btk5h.skriptmirror.SkriptMirror");
            skriptReflectPresent = true;
        } catch (ClassNotFoundException ignored) {}
        String eventName = "NexoFurnitureInteract_Internal";
        String eventPattern = "interact with (custom|Nexo) furniture [%string%]";
        if (!skriptReflectPresent) {
            register(eventName, eventPattern);
        } else {
            register(eventName + "_Reflect", eventPattern);
        }
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