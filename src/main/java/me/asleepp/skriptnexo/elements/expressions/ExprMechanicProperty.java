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

@Name("Mechanic Property")
@Description("Gets a property of a custom Nexo mechanic")
@Examples({
    "set {_damage} to property \"damage\" of mechanic \"custom_mechanic\"",
    "set {_enabled} to mechanic \"custom_mechanic\"'s property \"enabled\""
})
public class ExprMechanicProperty extends SimpleExpression<Object> {
    static {
        Skript.registerExpression(ExprMechanicProperty.class, Object.class, ExpressionType.PROPERTY,
            "[the] property %string% of [mechanic] %string%",
            "[mechanic] %string%'s property %string%");
    }
}
