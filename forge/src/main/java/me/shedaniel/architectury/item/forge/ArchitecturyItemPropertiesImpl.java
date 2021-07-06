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

package me.shedaniel.architectury.item.forge;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

import me.shedaniel.architectury.item.ArchitecturyItemProperties;
import me.shedaniel.architectury.item.ItemPropertiesExtension;
import net.minecraftforge.common.ToolType;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class ArchitecturyItemPropertiesImpl {
    public static ItemPropertiesExtension create() {
        return new Impl();
    }

    private static final class Impl extends ArchitecturyItemProperties {
        @Override
        public ItemPropertiesExtension withEquipmentSlot(Function<ItemStack, EquipmentSlot> function) {
            return this;
        }

        @Override
        public ItemPropertiesExtension withCustomDamageHandler(CustomDamageHandler handler) {
            return this;
        }

        @Override
        public ItemPropertiesExtension cannotRepair() {
            return (ArchitecturyItemProperties) this.setNoRepair();
        }

        @Override
        public ItemPropertiesExtension asToolType(me.shedaniel.architectury.registry.ToolType type, int level) {
            return (ArchitecturyItemProperties) this.addToolType(ToolType.get(type.forgeName), level);
        }

        @Override
        public ItemPropertiesExtension withCustomRenderer(Supplier<Callable<BlockEntityWithoutLevelRenderer>> ister) {
            return (ArchitecturyItemProperties) this.setISTER(ister);
        }
    }
}
