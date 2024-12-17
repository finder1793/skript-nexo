package me.asleepp.skriptnexo.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.nexomc.nexo.api.events.resourcepack.NexoPrePackGenerateEvent;
import org.bukkit.event.Event;
import team.unnamed.creative-api.ResourcePack;

import javax.annotation.Nullable;

@Name("On Pre Pack Generate")
@Description({"Fires before the resource pack is generated."})
@Examples({"on pre pack generate:"})
@Since("1.0")
public class EvtPrePackGenerateEvent extends SkriptEvent {

    static {
        Skript.registerEvent("Pre Pack Generate", EvtPrePackGenerateEvent.class, NexoPrePackGenerateEvent.class, "pre pack generate");
        EventValues.registerEventValue(NexoPrePackGenerateEvent.class, ResourcePack.class, new Getter<ResourcePack, NexoPrePackGenerateEvent>() {
            @Override
            public ResourcePack get(NexoPrePackGenerateEvent event) {
                return event.getResourcePack();
            }
        }, 0);
    }

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(Event e) {
        return e instanceof NexoPrePackGenerateEvent;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "pre pack generate";
    }
}