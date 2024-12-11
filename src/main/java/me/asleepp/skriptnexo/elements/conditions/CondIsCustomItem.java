package me.asleepp.skriptnexo.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.nexomc.nexo.api.NexoItems;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

@Name("Is Nexo Item")
@Description({"Checks if the Item is an Nexo item."})
@Examples({"if player's tool is a custom item"})
@Since("1.0")
@RequiredPlugins("Nexo")
public class CondIsCustomItem extends Condition {

    private Expression<ItemType> item;

    static {
        Skript.registerCondition(CondIsCustomItem.class, "%itemtypes% (is [a[n]]|are) (custom|nexo) item[s]");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        item = (Expression<ItemType>) exprs[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        ItemType[] items = item.getArray(e);

        for (ItemType itemType : items) {
            ItemStack itemStack = itemType.getRandom();

            boolean exists = NexoItems.exists(itemStack);
            if (!exists) {
                return false;
            }
        }
        return true;
    }
    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return item.toString(e, debug) + " is an Oraxen item";
    }

}
