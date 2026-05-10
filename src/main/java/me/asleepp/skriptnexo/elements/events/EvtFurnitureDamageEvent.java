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
import com.nexomc.nexo.api.events.furniture.NexoFurnitureDamageEvent;
import me.asleepp.skriptnexo.SkriptNexo;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("On Custom Furniture Damage")
@Description({"Fires when an Nexo furniture gets damaged."})
@Examples({"on damage of custom furniture:"})
@Since("1.0")
public class EvtFurnitureDamageEvent extends SkriptEvent {

    private Literal<String> furnitureID;
    private static boolean skriptReflectPresent = false;

    @SuppressWarnings("unchecked")
    private static void register(String name, String pattern) {
        SyntaxRegistry syntaxRegistry = SkriptNexo.getAddonInstance().syntaxRegistry();
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY,
            BukkitSyntaxInfos.Event.builder(EvtFurnitureDamageEvent.class, name)
                .addEvent(NexoFurnitureDamageEvent.class)
                .addPatterns(pattern)
                .supplier(EvtFurnitureDamageEvent::new)
                .build());
        EventValueRegistry evr = SkriptNexo.getAddonInstance().registry(EventValueRegistry.class);
        evr.register(EventValue.simple(NexoFurnitureDamageEvent.class, Player.class, NexoFurnitureDamageEvent::getPlayer));
    }

    static {
        try {
            Class.forName("com.btk5h.skriptmirror.SkriptMirror");
            skriptReflectPresent = true;
        } catch (ClassNotFoundException ignored) {}

        String eventName = "NexoFurnitureDamage_Internal";
        String eventPattern = "(hurt|damage) of (custom|Nexo) furniture [%string%]";
        if (!skriptReflectPresent) {
            register(eventName, eventPattern);
        } else {
            register(eventName + "_Reflect", eventPattern);
        }
    }
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        furnitureID = (Literal<String>) args[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (e instanceof NexoFurnitureDamageEvent) {
            NexoFurnitureDamageEvent event = (NexoFurnitureDamageEvent) e;
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
        return "custom furniture" + (furnitureID != null ? furnitureID.toString(e, debug) : "");
    }
}
