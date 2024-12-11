package me.asleepp.skriptnexo.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.nexomc.nexo.api.NexoBlocks;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("Place Nexo Block")
@Description({"Places an Nexo block at a location."})
@Examples({"set block at player's location to custom block \"amethyst_ore\""})
@Since("1.0")
@RequiredPlugins("Nexo")
public class EffPlaceBlock extends Effect {

    private Expression<Location> locationExpr;
    private Expression<String> nexoBlockId;

    static {
        Skript.registerEffect(EffPlaceBlock.class, "(set|place) block at %locations% to (custom|nexo) block %string%");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        locationExpr = (Expression<Location>) exprs[0];
        if (exprs.length > 1 && exprs[1] != null) {
            nexoBlockId = (Expression<String>) exprs[1];
        }
        return true;
    }

    @Override
    protected void execute(Event e) {
        Location[] locations = locationExpr.getArray(e);
        String blockId = nexoBlockId.getSingle(e);

        if (blockId == null) {
            return;
        }

        for (Location location : locations) {
            if (NexoBlocks.isCustomBlock(blockId)) {
                NexoBlocks.place(blockId, location);
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "place custom block" + nexoBlockId.toString(e, debug) + "at" + locationExpr.toString(e, debug);
    }
}
