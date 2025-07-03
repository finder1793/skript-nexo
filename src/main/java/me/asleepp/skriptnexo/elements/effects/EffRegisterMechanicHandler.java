package me.asleepp.skriptnexo.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import me.asleepp.skriptnexo.mechanics.MechanicHandler;
import org.bukkit.event.Event;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Since;
import org.jetbrains.annotations.Nullable;

@Name("Register Mechanic Handler")
@Description("Registers a handler for a specific mechanic event")
@Examples({
    "register handler for mechanic \"toasty\" event \"interact\":",
    "    send \"You interacted with a toasty mechanic!\" to player"
})
@Since("1.0")
public class EffRegisterMechanicHandler extends Effect {
    private Expression<String> mechanicId;
    private Expression<String> eventType;
    private TriggerItem trigger;

    static {
        Skript.registerEffect(EffRegisterMechanicHandler.class, 
            "register handler for [mechanic] %string% [event] %string%");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        mechanicId = (Expression<String>) exprs[0];
        eventType = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String id = mechanicId.getSingle(e);
        String event = eventType.getSingle(e);
        if (id == null || event == null || trigger == null) return;

        // Register the handler with the MechanicHandler
        MechanicHandler.registerHandler(id, event, trigger);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "register handler for mechanic " + mechanicId.toString(e, debug) + 
               " event " + eventType.toString(e, debug);
    }

    public TriggerItem setNext(TriggerItem next) {
        this.trigger = next;
        return next;
    }

    public TriggerItem getNext() {
        return trigger;
    }
}
