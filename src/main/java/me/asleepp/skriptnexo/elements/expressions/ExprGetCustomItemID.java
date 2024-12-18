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
import com.nexomc.nexo.api.NexoItems;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import ch.njol.skript.lang.ExpressionType;

@Name("Get Custom Item ID from ItemStack")
@Description("Gets the custom item ID from an item stack.")
@Examples({"set {_id} to custom item ID of player's tool"})
@Since("1.0")
public class ExprGetCustomItemIDFromItemStack extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprGetCustomItemID.class, String.class, ExpressionType.PROPERTY, "(custom|nexo) item ID of %itemstack%");
    }

    private Expression<ItemStack> itemStackExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        itemStackExpr = (Expression<ItemStack>) exprs[0];
        return true;
    }

    @Override
    protected String[] get(Event e) {
        ItemStack itemStack = itemStackExpr.getSingle(e);
        if (itemStack == null) {
            return null;
        }
        String itemId = NexoItems.idFromItem(itemStack);
        return itemId != null ? new String[]{itemId} : null;
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
        return "custom item ID of " + itemStackExpr.toString(e, debug);
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return null;
    }
}