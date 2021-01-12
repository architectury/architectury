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

package me.shedaniel.architectury.item.fabric;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

import me.shedaniel.architectury.item.ArchitecturyItemProperties;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

public class ArchitecturyItemPropertiesPlatformImpl {
    // generic

    static Item.Properties getPlatformProperties() {
        return new FabricItemSettings();
    }

    // fabric

    static void equipmentSlot(Item.Properties properties, Function<ItemStack, EquipmentSlot> function) {
        ((FabricItemSettings) properties).equipmentSlot(function::apply);
    }

    static void customDamage(Item.Properties properties, ArchitecturyItemProperties.CustomDamageHandler handler) {
        ((FabricItemSettings) properties).customDamage(handler::damage);
    }

    // forge

    static void setNoRepair(Item.Properties properties) {
    }

    static void addToolType(Item.Properties properties, String forgeName, int level) {
    }

    static void setISTER(Item.Properties properties, Supplier<Callable<BlockEntityWithoutLevelRenderer>> ister) {
    }
}
