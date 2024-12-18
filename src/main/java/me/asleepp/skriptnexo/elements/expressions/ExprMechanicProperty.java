package me.asleepp.skriptnexo.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import org.jetbrains.annotations.Nullable;
import me.asleepp.skriptnexo.mechanics.SkriptMechanicFactory;
import com.nexomc.nexo.mechanics.MechanicsManager;

@Name("Mechanic Property")
@Description("Gets a property of a custom Nexo mechanic")
@Examples({
    "set {_damage} to property \"damage\" of mechanic \"custom_mechanic\"",
    "set {_enabled} to mechanic \"custom_mechanic\"'s property \"enabled\""
})
public class ExprMechanicProperty extends SimpleExpression<Object> {
    private Expression<String> propertyName;
    private Expression<String> mechanicId;

    static {
        Skript.registerExpression(ExprMechanicProperty.class, Object.class, ExpressionType.PROPERTY,
            "[the] property %string% of [mechanic] %string%",
            "[mechanic] %string%'s property %string%");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (matchedPattern == 0) {
            propertyName = (Expression<String>) exprs[0];
            mechanicId = (Expression<String>) exprs[1];
        } else {
            mechanicId = (Expression<String>) exprs[0];
            propertyName = (Expression<String>) exprs[1];
        }
        return true;
    }

    @Override
    protected Object[] get(Event e) {
        String id = mechanicId.getSingle(e);
        String property = propertyName.getSingle(e);
        if (id == null || property == null) return null;
        
        SkriptMechanicFactory factory = (SkriptMechanicFactory) MechanicsManager.INSTANCE.getMechanicFactory(id);
        if (factory == null) return null;
        
        return new Object[]{factory.getProperty(property)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Object> getReturnType() {
        return Object.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "property " + propertyName.toString(e, debug) + " of mechanic " + mechanicId.toString(e, debug);
    }
}
