package me.asleepp.skriptnexo.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.nexomc.nexo.mechanics.Mechanic;
import com.nexomc.nexo.mechanics.MechanicFactory;
import com.nexomc.nexo.mechanics.MechanicsManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import ch.njol.skript.lang.ExpressionType;

import java.io.StringReader;

@Name("Create Mechanic")
@Description("Creates a new mechanic with the specified configuration.")
@Examples({"set {_mechanic} to create mechanic with id \"custom_mechanic\" and configuration \"key: value\""})
@Since("1.0")
public class ExprCreateMechanic extends SimpleExpression<Mechanic> {

    static {
        Skript.registerExpression(ExprCreateMechanic.class, Mechanic.class, ExpressionType.SIMPLE, "create mechanic with id %string% and configuration %string%");
    }

    private Expression<String> mechanicIdExpr;
    private Expression<String> configExpr;

    public ExprCreateMechanic() {
        // No-argument constructor
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        mechanicIdExpr = (Expression<String>) exprs[0];
        configExpr = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected Mechanic[] get(Event e) {
        String mechanicId = mechanicIdExpr.getSingle(e);
        String configString = configExpr.getSingle(e);
        if (mechanicId == null || configString == null) {
            return null;
        }

        ConfigurationSection configSection = loadConfigurationFromString(configString);
        MechanicFactory factory = MechanicsManager.INSTANCE.getMechanicFactory(mechanicId);
        if (factory == null) {
            return null;
        }

        Mechanic mechanic = factory.parse(configSection);
        return mechanic != null ? new Mechanic[]{mechanic} : null;
    }

    private ConfigurationSection loadConfigurationFromString(String configString) {
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        try {
            yamlConfiguration.load(new StringReader(configString));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return yamlConfiguration;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Mechanic> getReturnType() {
        return Mechanic.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "create mechanic with id " + mechanicIdExpr.toString(e, debug) + " and configuration " + configExpr.toString(e, debug);
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return null;
    }
}