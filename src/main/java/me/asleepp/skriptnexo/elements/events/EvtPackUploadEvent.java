package me.asleepp.skriptnexo.elements.events;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;
import com.nexomc.nexo.api.events.resourcepack.NexoPackUploadEvent;
import me.asleepp.skriptnexo.SkriptNexo;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("On Pack Upload")
@Description({"Fires when the resource pack is uploaded."})
@Examples({"on pack upload:"})
@Since("1.0")
public class EvtPackUploadEvent extends SkriptEvent {

    @SuppressWarnings("unchecked")
    private static void register() {
        SyntaxRegistry syntaxRegistry = SkriptNexo.getAddonInstance().syntaxRegistry();
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY,
            BukkitSyntaxInfos.Event.builder(EvtPackUploadEvent.class, "Pack Upload")
                .addEvent(NexoPackUploadEvent.class)
                .addPatterns("pack upload")
                .supplier(EvtPackUploadEvent::new)
                .build());
        EventValueRegistry evr = SkriptNexo.getAddonInstance().registry(EventValueRegistry.class);
        evr.register(EventValue.simple(NexoPackUploadEvent.class, String.class, NexoPackUploadEvent::getHash));
        evr.register(EventValue.simple(NexoPackUploadEvent.class, String.class, NexoPackUploadEvent::getUrl));
    }

    static { register(); }

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