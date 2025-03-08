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
import org.skriptlang.skript.lang.converter.Converter;
import com.nexomc.nexo.api.events.resourcepack.NexoPackUploadEvent;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("On Pack Upload")
@Description({"Fires when the resource pack is uploaded."})
@Examples({"on pack upload:"})
@Since("1.0")
public class EvtPackUploadEvent extends SkriptEvent {

    static {
        Skript.registerEvent("Pack Upload", EvtPackUploadEvent.class, NexoPackUploadEvent.class, "pack upload");
        EventValues.registerEventValue(NexoPackUploadEvent.class, String.class, new Converter<NexoPackUploadEvent, String>() {
            @Override
            public String convert(NexoPackUploadEvent event) {
                return event.getHash();
            }
        }, 0);
        EventValues.registerEventValue(NexoPackUploadEvent.class, String.class, new Converter<NexoPackUploadEvent, String>() {
            @Override
            public String convert(NexoPackUploadEvent event) {
                return event.getUrl();
            }
        }, 0);
    }

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(Event e) {
        return e instanceof NexoPackUploadEvent;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "pack upload";
    }
}