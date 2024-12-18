package me.asleepp.skriptnexo.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.nexomc.nexo.mechanics.MechanicFactory;
import com.nexomc.nexo.mechanics.MechanicsManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Since;
import org.jetbrains.annotations.Nullable;
import me.asleepp.skriptnexo.SkriptNexo;
import me.asleepp.skriptnexo.mechanics.SkriptMechanicFactory;

import java.io.File;
import java.io.IOException;

@Name("Create Custom Mechanic")
@Description("Creates a custom Nexo mechanic with the specified ID and configuration")
@Examples({
    "create nexo mechanic with id \"custom_mechanic\":",
    "# mechanic configuration here"
})
@Since("1.0")
public class EffCreateCustomMechanic extends Effect {

    private Expression<String> mechanicId;
    
    static {
        Skript.registerEffect(EffCreateCustomMechanic.class, 
            "create [new] [nexo] mechanic [with id] %string%");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        mechanicId = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String id = mechanicId.getSingle(e);
        if (id == null) return;
        
        // Create mechanics directory if it doesn't exist
        File mechanicsDir = new File(SkriptNexo.getInstance().getDataFolder(), "mechanics");
        if (!mechanicsDir.exists()) {
            mechanicsDir.mkdirs();
        }

        // Create/load mechanic config file
        File mechanicFile = new File(mechanicsDir, id + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(mechanicFile);
        
        // Set default values if file is new
        if (!mechanicFile.exists()) {
            config.set("enabled", true);
            config.set("mechanic-id", id);
            try {
                config.save(mechanicFile);
            } catch (IOException ex) {
                SkriptNexo.getInstance().getLogger().warning("Failed to save mechanic configuration for " + id);
            }
        }

        MechanicFactory factory = new SkriptMechanicFactory(id);
        MechanicsManager.INSTANCE.registerMechanicFactory(factory, true);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "create new nexo mechanic with id " + mechanicId.toString(e, debug);
    }
}
