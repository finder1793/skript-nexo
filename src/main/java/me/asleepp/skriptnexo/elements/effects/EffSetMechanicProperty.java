package me.asleepp.skriptnexo.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import org.jetbrains.annotations.Nullable;
import me.asleepp.skriptnexo.mechanics.SkriptMechanicFactory;
import com.nexomc.nexo.mechanics.MechanicsManager;

@Name("Set Mechanic Property")
@Description("Sets a property for a custom Nexo mechanic")
@Examples({
    "set property \"enabled\" of mechanic \"custom_mechanic\" to true",
    "set mechanic \"custom_mechanic\"'s property \"damage\" to 10"
})
public class EffSetMechanicProperty extends Effect {
    private Expression<String> propertyName;
    private Expression<String> mechanicId;
    private Expression<Object> value;

    static {
        Skript.registerEffect(EffSetMechanicProperty.class,
            "set [the] property %string% of [mechanic] %string% to %object%",
            "set [mechanic] %string%'s property %string% to %object%");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (matchedPattern == 0) {
            propertyName = (Expression<String>) exprs[0];
            mechanicId = (Expression<String>) exprs[1];
            value = (Expression<Object>) exprs[2];
        } else {
            mechanicId = (Expression<String>) exprs[0];
            propertyName = (Expression<String>) exprs[1];
            value = (Expression<Object>) exprs[2];
        }
        return true;
    }

    @Override
    protected void execute(Event e) {
        String id = mechanicId.getSingle(e);
        String property = propertyName.getSingle(e);

        // Get the value and handle UnparsedLiteral conversion
        Object val = null;
        try {
            val = value.getSingle(e);
        } catch (Exception ex) {
            Skript.error("Error getting value: " + ex.getMessage());
            return;
        }

        if (id == null || property == null || val == null) return;

        SkriptMechanicFactory factory = (SkriptMechanicFactory) MechanicsManager.INSTANCE.getMechanicFactory(id);
        if (factory != null) {
            factory.setProperty(property, val);
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "set property " + propertyName.toString(e, debug) + " of mechanic " + 
               mechanicId.toString(e, debug) + " to " + value.toString(e, debug);
    }
}
