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

        if (id == null || property == null) return;

        // Get the value and handle UnparsedLiteral conversion
        Object val = null;
        try {
            // First try to get the value directly
            val = value.getSingle(e);
        } catch (Exception ex) {
            // If that fails, try to get the string representation
            try {
                String stringVal = value.toString(e, false);

                // Remove quotes if present (common with string literals)
                if (stringVal.startsWith("\"") && stringVal.endsWith("\"")) {
                    stringVal = stringVal.substring(1, stringVal.length() - 1);
                }

                // Try to convert to appropriate type based on the string value
                if (stringVal.equalsIgnoreCase("true") || stringVal.equalsIgnoreCase("false")) {
                    val = Boolean.parseBoolean(stringVal);
                } else {
                    try {
                        // Try to parse as a number
                        if (stringVal.contains(".")) {
                            val = Double.parseDouble(stringVal);
                        } else {
                            val = Integer.parseInt(stringVal);
                        }
                    } catch (NumberFormatException numEx) {
                        // If not a number, use as string
                        val = stringVal;
                    }
                }
            } catch (Exception ex2) {
                Skript.error("Error converting value: " + ex.getMessage() + ", " + ex2.getMessage());
                return;
            }
        }

        if (val == null) {
            Skript.error("Could not get value for property '" + property + "'");
            return;
        }

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
