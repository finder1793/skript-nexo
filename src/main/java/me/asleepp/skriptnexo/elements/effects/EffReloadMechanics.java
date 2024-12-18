package me.asleepp.skriptnexo.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import com.nexomc.nexo.mechanics.MechanicFactory;
import com.nexomc.nexo.mechanics.MechanicsManager;
import me.asleepp.skriptnexo.SkriptNexo;
import me.asleepp.skriptnexo.mechanics.SkriptMechanicFactory;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.io.File;


@Name("Reload Mechanics")
@Description("Reloads all custom mechanics")
@Examples({
    "reload nexo mechanics",
    "reload all mechanics"
})
public class EffReloadMechanics extends Effect {

    static {
        Skript.registerEffect(EffReloadMechanics.class, 
            "reload [all] [nexo] mechanics");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected void execute(Event e) {
        MechanicsManager.INSTANCE.unregisterMechanicFactory("skript");
        File mechanicsDir = new File(SkriptNexo.getInstance().getDataFolder(), "mechanics");
        if (mechanicsDir.exists()) {
            for (File file : mechanicsDir.listFiles()) {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                String id = config.getString("mechanic-id");
                if (id != null) {
                    MechanicFactory factory = new SkriptMechanicFactory(id);
                    MechanicsManager.INSTANCE.registerMechanicFactory(factory, true);
                }
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "reload nexo mechanics";
    }
}
