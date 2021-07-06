/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 shedaniel
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

package me.shedaniel.architectury.mixin.fabric.client;

import me.shedaniel.architectury.item.fabric.ArchitecturyItemPropertiesImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.item.Item;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;

@Mixin(Item.class)
public class MixinItem {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void addForgeItemProperties(Item.Properties properties, CallbackInfo info) {
        if (properties instanceof ArchitecturyItemPropertiesImpl.Impl) {
            try {
                BuiltinItemRendererRegistry.INSTANCE.register((Item) (Object) this, ((ArchitecturyItemPropertiesImpl.Impl) properties).ister.get().call()::renderByItem);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
