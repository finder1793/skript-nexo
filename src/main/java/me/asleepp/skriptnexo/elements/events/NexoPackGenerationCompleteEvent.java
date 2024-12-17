package com.nexomc.nexo.api.events.resourcepack;

import com.nexomc.nexo.api.NexoPack;
import com.nexomc.nexo.pack.creative.NexoPackReader;
import com.nexomc.nexo.utils.printOnFailure;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import java.io.File;

/**
 * Fires after the resource pack generation is complete.
 */
public class NexoPackGenerationCompleteEvent extends Event {
    private final ResourcePack resourcePack;

    public NexoPackGenerationCompleteEvent(ResourcePack resourcePack) {
        this.resourcePack = resourcePack;
    }

    public ResourcePack getResourcePack() {
        return resourcePack;
    }

    public boolean addResourcePack(ResourcePack resourcePack) {
        return runCatching(() -> {
            NexoPack.mergePack(this.resourcePack, resourcePack);
            return true;
        }).printOnFailure().getOrNull() != null;
    }

    public boolean addResourcePack(File resourcePack) {
        return runCatching(() -> {
            NexoPack.mergePack(this.resourcePack, NexoPackReader().readFile(resourcePack));
            return true;
        }).printOnFailure().getOrNull() != null;
    }

    public boolean addUnknownFile(String path, byte[] data) {
        return runCatching(() -> {
            this.resourcePack.unknownFile(path, Writable.bytes(data));
            return true;
        }).printOnFailure().getOrNull() != null;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    private static final HandlerList handlerList = new HandlerList();
}