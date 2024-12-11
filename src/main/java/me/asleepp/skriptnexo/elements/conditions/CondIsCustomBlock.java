package me.asleepp.skriptnexo.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.nexomc.nexo.api.NexoBlocks;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Is Nexo Block")
@Description({"Checks if the block is an Nexo block."})
@Examples({
    "on break:",
        "\tif event-block is a custom block",
            "\t\tsend \"you killed my pet block :(\" to player"
})
@Since("1.0")
@RequiredPlugins("Nexo")
public class CondIsCustomBlock extends Condition {

    private Expression<Block> block;

    static {
        Skript.registerCondition(CondIsCustomBlock.class,"%blocks% (is [a[n]]|are) (custom|nexo) block[s]");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        block = (Expression<Block>) exprs[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        Block[] blocks = block.getArray(e);

        for (Block b : blocks) {
            if (NexoBlocks.isCustomBlock(b)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return block.toString(e, debug) + " is an Nexo block";
    }
}
