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
import com.nexomc.nexo.api.events.furniture.NexoFurnitureDamageEvent;
import com.nexomc.nexo.api.events.furniture.NexoFurnitureInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("On Custom Furniture Damage")
@Description({"Fires when an Nexo furniture gets damaged."})
@Examples({"on damage of custom furniture:"})
@Since("1.0")
public class EvtFurnitureDamageEvent extends SkriptEvent {

    private Literal<String> furnitureID;

    static{
        Skript.registerEvent("Furniture Damage", EvtFurnitureDamageEvent.class, NexoFurnitureDamageEvent.class, "(hurt|damage) of (custom|Nexo) furniture [%string%]");
        EventValues.registerEventValue(NexoFurnitureDamageEvent.class, Player.class, new Getter<Player, NexoFurnitureDamageEvent>() {
            @Override
            public Player get(NexoFurnitureDamageEvent arg) {
                return arg.getPlayer();
            }
        }, 0);
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
