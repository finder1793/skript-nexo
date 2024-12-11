package me.asleepp.skriptnexo.elements.effects;

import me.asleepp.skriptnexo.elements.effects.mechanicbuilder.MechanicLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import org.bukkit.event.Event;
import java.util.Map;
import com.nexomc.nexo.mechanics.Mechanic;
import com.nexomc.nexo.mechanics.MechanicFactory;
import org.yaml.snakeyaml.Yaml;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import com.nexomc.nexo.mechanics.MechanicsManager;
 
@Name("Register New Nexo Mechanic")
@Description({"Register New Nexo Mechanic to use at your leasure"})
@Examples({"create mechanic"})
@Since("1.0.5")
@RequiredPlugins("Nexo")

public class EffRegisterMechanic extends Effect {
    private String mechanicName;
    private Map<String, Object> mechanicConfig;
 
    static  {
        Skript.registerEffect(EffRegisterMechanic.class, "create mechanic %string% with: %objects%");
    }

    @Override
    protected void execute(Event event) {
        // Pass the mechanic name and configuration to Nexo
        new MechanicLoader().loadMechanic(mechanicName, mechanicConfig);
    }
 
    @Override
    public String toString(Event event, boolean debug) {
        return "Register mechanic: " + mechanicName;
    }
}

public class MechanicYamlUpdater {
    private static final String MECHANICS_FILE = "path/to/mechanics.yml";
 
    public void addMechanicToFile(String mechanicName, Map<String, Object> config) {
        Yaml yaml = new Yaml();
        Map<String, Object> currentData = yaml.load(new FileInputStream(MECHANICS_FILE));
 
        // Add the new mechanic under the "custom" section
        Map<String, Object> customMechanics = (Map<String, Object>) currentData.get("custom");
        customMechanics.put(mechanicName, config);
 
        // Save back to YAML
        try (FileWriter writer = new FileWriter(MECHANICS_FILE)) {
            yaml.dump(currentData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
]
public class MechanicLoader {
    public void loadMechanic(String mechanicName, Map<String, Object> config) {
        // Converts the Mechanic to a format Nexo understands
        Mechanic newMechanic = new Mechanic(mechanicName, config);

        // Use Nexo Registry to add Mechanic
        MechanicFactory(newMechanic);
    }
}