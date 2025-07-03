package me.asleepp.skriptnexo.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.nexomc.nexo.api.NexoItems;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class CondHasMechanic extends Condition {
    private Expression<ItemType> itemExpr;
    private Expression<String> mechanicIdExpr;

    static {
        Skript.registerCondition(CondHasMechanic.class,
            "%itemtype% has mechanic %string%",
            "%itemtype% (doesn't|does not) have mechanic %string%");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        itemExpr = (Expression<ItemType>) exprs[0];
        mechanicIdExpr = (Expression<String>) exprs[1];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(Event e) {
        ItemType itemType = itemExpr.getSingle(e);
        String mechanicId = mechanicIdExpr.getSingle(e);
        if (itemType == null || mechanicId == null) return false;

        ItemStack itemStack = itemType.getRandom();
        if (itemStack == null) return false;

        // Get the item ID from the ItemStack
        String itemId = NexoItems.idFromItem(itemStack);
        if (itemId == null) return isNegated(); // If the item doesn't have an ID, it's not a Nexo item

        // Check if the item has the mechanic using NexoItems API
        boolean hasMechanic = NexoItems.hasMechanic(itemId, mechanicId);
        return isNegated() ? !hasMechanic : hasMechanic;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return itemExpr.toString(e, debug) + (isNegated() ? " doesn't have mechanic " : " has mechanic ") + 
               mechanicIdExpr.toString(e, debug);
    }
}
