package me.asleepp.skriptoraxen.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import io.th0rgal.oraxen.api.OraxenBlocks;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("Is Nexo Block")
@Description({"Checks if the block is an Nexo block."})
@Examples({
        "on break:",
        "\tif event-block is a custom block",
        "\t\tsend \"you killed my pet block :(\" to player"})
public class CondIsCustomBlock extends Condition {
    private Expression<Block> block;

    static {
        Skript.registerCondition(CondIsCustomBlock.class,"%blocks% (is [a[n]]|are) (custom|oraxen) block[s]");
    }

    @Override
    public boolean check(Event e) {
        Block[] blocks = block.getArray(e);
        if (blocks == null) {
            return false;
        }

        for (Block b : blocks) {
            if (NexoBlocks.isNexoBlock(b)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return block.toString(e, debug) + " is an Nexo block";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        block = (Expression<Block>) exprs[0];
        return true;
    }
}
