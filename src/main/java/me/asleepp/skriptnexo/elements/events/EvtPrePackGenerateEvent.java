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
import com.nexomc.nexo.api.events.resourcepack.NexoPrePackGenerateEvent;
import me.asleepp.skriptnexo.SkriptNexo;
import org.bukkit.event.Event;
import team.unnamed.creative.ResourcePack;

import javax.annotation.Nullable;

@Name("On Pre Pack Generate")
@Description({"Fires before the resource pack is generated."})
@Examples({"on pre pack generate:"})
@Since("1.0")
public class EvtPrePackGenerateEvent extends SkriptEvent {

    @SuppressWarnings("unchecked")
    private static void register() {
        SyntaxRegistry syntaxRegistry = SkriptNexo.getAddonInstance().syntaxRegistry();
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY,
            BukkitSyntaxInfos.Event.builder(EvtPrePackGenerateEvent.class, "Pre Pack Generate")
                .addEvent(NexoPrePackGenerateEvent.class)
                .addPatterns("pre pack generate")
                .supplier(EvtPrePackGenerateEvent::new)
                .build());
        EventValueRegistry evr = SkriptNexo.getAddonInstance().registry(EventValueRegistry.class);
        evr.register(EventValue.simple(NexoPrePackGenerateEvent.class, ResourcePack.class, NexoPrePackGenerateEvent::getResourcePack));
    }

    static { register(); }

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