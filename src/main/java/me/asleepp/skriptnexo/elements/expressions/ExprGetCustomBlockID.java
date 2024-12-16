package me.asleepp.skriptnexo.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.mechanics.custom_block.CustomBlockMechanic;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import ch.njol.skript.lang.ExpressionType;

@Name("Get Custom Block ID")
@Description("Gets the custom block ID from a block.")
@Examples({"set {_id} to custom block ID of target block"})
@Since("1.0")
public class ExprGetCustomBlockID extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprGetCustomBlockID.class, String.class, ExpressionType.PROPERTY, "(custom|nexo) block ID of %block%");
    }

    private Expression<Block> blockExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        blockExpr = (Expression<Block>) exprs[0];
        return true;
    }

    @Override
    protected String[] get(Event e) {
        Block block = blockExpr.getSingle(e);
        if (block == null) {
            return null;
        }
        CustomBlockMechanic mechanic = NexoBlocks.customBlockMechanic(block.getLocation());
        if (mechanic == null) {
            return null;
        }
        String blockId = mechanic.getItemID();
        return blockId != null ? new String[]{blockId} : null;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "custom block ID of " + blockExpr.toString(e, debug);
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return null;
    }
}