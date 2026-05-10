package me.asleepp.skriptnexo.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import com.nexomc.nexo.api.NexoItems;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.asleepp.skriptnexo.SkriptNexo;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;


import javax.annotation.Nullable;
@Name("Nexo Item")
@Description({"Gets an Nexo item."})
@Examples({"give player nexo item \"amethyst\""})
@Since("1.0")
@RequiredPlugins("Nexo")
public class ExprGetCustomItem extends SimpleExpression<ItemType> {

    private Expression<String> id;

    static {
        SyntaxRegistry registry = SkriptNexo.getAddonInstance().syntaxRegistry();
        registry.register(SyntaxRegistry.EXPRESSION,
            SyntaxInfo.Expression.builder(ExprGetCustomItem.class, ItemType.class)
                .priority(SyntaxInfo.SIMPLE)
                .addPatterns("(custom|nexo) item %string%")
                .supplier(ExprGetCustomItem::new)
                .build());
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        id = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected ItemType[] get(Event e) {
        String nexoId = id.getSingle(e);
        if (NexoItems.exists(nexoId)) {
            ItemStack item = NexoItems.itemFromId(nexoId).build();
            return new ItemType[]{new ItemType(item)};
        }
        return null;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Nexo item " + id.toString(e, debug);
    }
}
