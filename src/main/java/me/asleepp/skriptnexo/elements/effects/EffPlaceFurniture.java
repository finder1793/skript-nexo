package me.asleepp.skriptnexo.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.nexomc.nexo.api.NexoFurniture;
import org.bukkit.event.Event;
import org.bukkit.Location;

import javax.annotation.Nullable;
@Name("Place Nexo Furniture")
@Description({"Places a Nexo furniture at a location."})
@Examples({"set block at player's location to custom furniture \"chair\""})
public class EffPlaceFurniture extends Effect {

    private Expression<String> furnitureId;
    private Expression<Location> location;

    static {
        Skript.registerEffect(EffPlaceFurniture.class, "(set|place) [custom|nexo] furniture %string% at %location%");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        furnitureId = (Expression<String>) exprs[0];
        location = (Expression<Location>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String id = furnitureId.getSingle(e);
        Location loc = location.getSingle(e);

        if (id != null && loc != null) {
            NexoFurniture.place(id, loc.getBlock().getLocation(), loc.getYaw(), loc.getBlock().getFace(loc.getBlock()));
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "place furniture" + furnitureId.toString(e, debug) + "at" + location.toString(e, debug);
    }
}
