package me.asleepp.skriptnexo.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import com.nexomc.nexo.api.events.NexoMechanicsRegisteredEvent;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("On Mechanics Registered")
@Description({"Fires when all native mechanics are registered."})
@Examples({"on mechanics registered:"})
@Since("1.0")
public class EvtMechanicsRegisteredEvent extends SkriptEvent {

    static {
        Skript.registerEvent("Mechanics Registered", EvtMechanicsRegisteredEvent.class, NexoMechanicsRegisteredEvent.class, "mechanics registered");
    }

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(Event e) {
        return e instanceof NexoMechanicsRegisteredEvent;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "mechanics registered";
    }
}