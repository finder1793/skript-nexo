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
import org.bukkit.event.Event;
import ch.njol.skript.lang.ExpressionType;

@Name("Create Mechanic")
@Description("Creates a new mechanic with the specified configuration.")
@Examples({"set {_mechanic} to create mechanic with id \"custom_mechanic\" and configuration {_config}"})
@Since("1.0")
public class ExprCreateMechanic extends SimpleExpression<Mechanic> {

    static {
        Skript.registerExpression(ExprCreateMechanic.class, Mechanic.class, ExpressionType.SIMPLE, "create mechanic with id %string% and configuration %configurationsection%");
    }

    private Expression<String> mechanicIdExpr;
    private Expression<ConfigurationSection> configSectionExpr;
    private final MechanicsManager mechanicsManager;

    public ExprCreateMechanic(MechanicsManager mechanicsManager) {
        this.mechanicsManager = mechanicsManager;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        mechanicIdExpr = (Expression<String>) exprs[0];
        configSectionExpr = (Expression<ConfigurationSection>) exprs[1];
        return true;
    }

    @Override
    protected Mechanic[] get(Event e) {
        String mechanicId = mechanicIdExpr.getSingle(e);
        ConfigurationSection configSection = configSectionExpr.getSingle(e);
        if (mechanicId == null || configSection == null) {
            return null;
        }

        MechanicFactory factory = mechanicsManager.getMechanicFactory(mechanicId);
        if (factory == null) {
            return null;
        }

        Mechanic mechanic = factory.parse(configSection);
        return mechanic != null ? new Mechanic[]{mechanic} : null;
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
        return "create mechanic with id " + mechanicIdExpr.toString(e, debug) + " and configuration " + configSectionExpr.toString(e, debug);
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return null;
    }
}