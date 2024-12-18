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

@Name("Set Mechanic Property")
@Description("Sets a property for a custom Nexo mechanic")
@Examples({
    "set property \"enabled\" of mechanic \"custom_mechanic\" to true",
    "set mechanic \"custom_mechanic\"'s property \"damage\" to 10"
})
public class EffSetMechanicProperty extends Effect {
    static {
        Skript.registerEffect(EffSetMechanicProperty.class,
            "set [the] property %string% of [mechanic] %string% to %object%",
            "set [mechanic] %string%'s property %string% to %object%");
    }
}
