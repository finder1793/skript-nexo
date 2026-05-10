package me.asleepp.skriptnexo.elements.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.nexomc.nexo.api.NexoItems;
import me.asleepp.skriptnexo.SkriptNexo;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.ThrowableProjectile;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Get Custom Item ID from Projectile")
@Description("Gets the custom item ID from a projectile.")
@Examples({"set {_id} to custom item ID of projectile"})
@Since("1.1")
public class ExprGetCustomItemIDFromProjectile extends SimpleExpression<String> {

    static {
        SyntaxRegistry registry = SkriptNexo.getAddonInstance().syntaxRegistry();
        registry.register(SyntaxRegistry.EXPRESSION,
            SyntaxInfo.Expression.builder(ExprGetCustomItemIDFromProjectile.class, String.class)
                .priority(PropertyExpression.DEFAULT_PRIORITY)
                .addPatterns(
                    "(custom|nexo) item ID of %projectile%",
                    "%projectile%'s (custom|nexo) item ID")
                .supplier(ExprGetCustomItemIDFromProjectile::new)
                .build());
    }

    private Expression<Projectile> projectileExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        projectileExpr = (Expression<Projectile>) exprs[0];
        return true;
    }
    @Override
    protected String[] get(Event e) {
        Projectile projectile = projectileExpr.getSingle(e);
        if (projectile == null) {
            return null;
        }
        
        // Handle ThrowableProjectile items
        if (projectile instanceof ThrowableProjectile) {
            ItemStack item = ((ThrowableProjectile) projectile).getItem();
            if (item != null) {
                String nexoId = NexoItems.idFromItem(item);
                if (NexoItems.exists(nexoId)) {
                    return new String[]{nexoId};
                }
            }
        }
        
        // Fallback to metadata check
        if (projectile.hasMetadata("nexoItemId")) {
            for (MetadataValue value : projectile.getMetadata("nexoItemId")) {
                if (value != null) {
                    return new String[]{value.asString()};
                }
            }
        }
        
        return null;
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
        return "custom item ID of " + projectileExpr.toString(e, debug);
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return null;
    }
}