package me.asleepp.skriptnexo.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockInteractEvent;
import com.nexomc.nexo.api.events.custom_block.stringblock.NexoStringBlockInteractEvent;
import com.nexomc.nexo.api.events.furniture.NexoFurnitureInteractEvent;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;

import javax.annotation.Nullable;

@Name("Is Hand")
@Description({"Checks what hand the player interacted with a block/furniture is. Does not work on any other events other then Interaction events."})
@Examples({"if custom interaction was the player's left hand:"})
@Since("1.0")
@RequiredPlugins("Nexo")
public class CondIsHand extends Condition {

    private boolean isLeft;

    static {
        Skript.registerCondition(CondIsHand.class, "[custom|nexo] (interaction) (was [with]) [the[ir] | [the player's]] (:right|:left) (arm|hand)");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!getParser().isCurrentEvent(NexoFurnitureInteractEvent.class, NexoStringBlockInteractEvent.class, NexoNoteBlockInteractEvent.class)) {
            Skript.error("You can't use 'interaction is (:right|:left) hand' outside of a Custom Interaction event!");
            return false;
        }
        isLeft = parseResult.hasTag("left");
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (!(e instanceof NexoFurnitureInteractEvent) && !(e instanceof NexoStringBlockInteractEvent) && !(e instanceof NexoNoteBlockInteractEvent)) {
            return false;
        }

        EquipmentSlot hand = null;
        if (e instanceof NexoFurnitureInteractEvent event) {
            hand = event.getHand();
        } else if (e instanceof NexoStringBlockInteractEvent event) {
            hand = event.getHand();
        } else if (e instanceof NexoNoteBlockInteractEvent event) {
            hand = event.getHand();
        }

        if (isLeft) {
            return hand == EquipmentSlot.HAND;
        } else {
            return hand == EquipmentSlot.OFF_HAND;
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "interaction is " + (isLeft ? "left" : "right") + " click";
    }
}
