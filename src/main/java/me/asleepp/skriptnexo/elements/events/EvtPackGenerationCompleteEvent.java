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
import com.nexomc.nexo.api.events.resourcepack.NexoPostPackGenerateEvent;
import org.bukkit.event.Event;

@Name("On Pack Generation Complete")
@Description({"Fires when the resource pack generation is complete."})
@Examples({"on pack generation complete:"})
@Since("1.0")
public class EvtPackGenerationCompleteEvent extends SkriptEvent {

    static {
        Skript.registerEvent("Pack Generation Complete", EvtPackGenerationCompleteEvent.class, NexoPostPackGenerateEvent.class, "pack generation complete");
        EventValues.registerEventValue(NexoPostPackGenerateEvent.class, ResourcePack.class, new Getter<ResourcePack, NexoPostPackGenerateEvent>() {
            @Override
            public ResourcePack get(NexoPostPackGenerateEvent event) {
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
        return e instanceof NexoPostPackGenerateEvent;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "pack generation complete";
    }
}