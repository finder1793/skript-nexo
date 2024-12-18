package me.asleepp.skriptnexo.mechanics;

import com.nexomc.nexo.mechanics.Mechanic;
import com.nexomc.nexo.mechanics.MechanicFactory;
import org.bukkit.configuration.ConfigurationSection;
import java.util.HashMap;
import java.util.Map;

public class SkriptMechanicFactory extends MechanicFactory {
    private final String mechanicId;
    private final Map<String, Object> properties = new HashMap<>();

    public SkriptMechanicFactory(String mechanicId) {
        super(mechanicId);
        this.mechanicId = mechanicId;
    }

    @Override
    public Mechanic parse(ConfigurationSection section) {
        return new SkriptMechanic(this, section);
    }

    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }
}