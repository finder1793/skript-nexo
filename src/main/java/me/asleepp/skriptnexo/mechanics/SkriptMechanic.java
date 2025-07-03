package me.asleepp.skriptnexo.mechanics;

import com.nexomc.nexo.mechanics.Mechanic;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import me.asleepp.skriptnexo.mechanics.events.SkriptMechanicInteractEvent;

public class SkriptMechanic extends Mechanic {
    private final String mechanicId;

    public SkriptMechanic(SkriptMechanicFactory factory, ConfigurationSection section) {
        super(factory, section);
        this.mechanicId = factory.getMechanicId();
    }

    public String getMechanicId() {
        return mechanicId;
    }

    // Interaction methods to trigger custom handlers
    public boolean onInteract(Player player, Action action, ItemStack item) {
        // Create and execute a custom interaction event
        SkriptMechanicInteractEvent event = new SkriptMechanicInteractEvent(player, this, action, item);
        MechanicHandler.executeHandler(mechanicId, "interact", event);
        return !event.isCancelled();
    }
}
