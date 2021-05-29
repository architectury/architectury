/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package dev.architectury.mixin.fabric;

import dev.architectury.hooks.fabric.PersistentEntitySectionManagerHooks;
import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProgressListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public class MixinServerLevel {
    @Shadow @Final private PersistentEntitySectionManager<Entity> entityManager;
    
    @Inject(method = "save", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerChunkCache;save(Z)V"))
    private void save(ProgressListener progressListener, boolean bl, boolean bl2, CallbackInfo ci) {
        LifecycleEvent.SERVER_LEVEL_SAVE.invoker().act((ServerLevel) (Object) this);
    }
    
    @Inject(method = "addEntity", at = @At(value = "INVOKE",
                                           target = "Lnet/minecraft/world/level/entity/PersistentEntitySectionManager;addNewEntity(Lnet/minecraft/world/level/entity/EntityAccess;)Z"),
            cancellable = true)
    private void addEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        ((PersistentEntitySectionManagerHooks) this.entityManager).architectury_attachLevel((ServerLevel) (Object) this);
    }
}
