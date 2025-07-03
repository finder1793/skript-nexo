package me.asleepp.skriptnexo.mechanics;

import ch.njol.skript.lang.TriggerItem;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;

public class MechanicHandler {
    private static final Map<String, Map<String, TriggerItem>> mechanicHandlers = new HashMap<>();

    public static void registerHandler(String mechanicId, String eventType, TriggerItem trigger) {
        mechanicHandlers.computeIfAbsent(mechanicId, k -> new HashMap<>()).put(eventType, trigger);
    }

    public static void executeHandler(String mechanicId, String eventType, Event event) {
        Map<String, TriggerItem> handlers = mechanicHandlers.get(mechanicId);
        if (handlers != null && handlers.containsKey(eventType)) {
            try {
                TriggerItem trigger = handlers.get(eventType);
                java.lang.reflect.Method runMethod = ch.njol.skript.lang.TriggerItem.class.getDeclaredMethod("run", Event.class);
                runMethod.setAccessible(true);
                runMethod.invoke(trigger, event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean hasHandler(String mechanicId, String eventType) {
        Map<String, TriggerItem> handlers = mechanicHandlers.get(mechanicId);
        return handlers != null && handlers.containsKey(eventType);
    }
}
