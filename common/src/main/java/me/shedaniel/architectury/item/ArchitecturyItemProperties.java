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

package me.shedaniel.architectury.item;

import java.util.function.Consumer;

import me.shedaniel.architectury.annotations.ExpectPlatform;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * A wrapper for The {@link Item.Properties} class for both the fabric and forge environments. Deprecated methods in this class only work on one environment, so do not necessarily expect the same functionality.
 */
public abstract class ArchitecturyItemProperties extends Item.Properties implements ItemPropertiesExtension {
    protected ArchitecturyItemProperties () {}

    @ExpectPlatform
    public static ItemPropertiesExtension create() {
        throw new AssertionError();
    }

    /*
     * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *     http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
    @FunctionalInterface
    public interface CustomDamageHandler {
        /**
         * Called to apply damage to the given stack.
         * This can be used to e.g. drain from a battery before actually damaging the item.
         *
         * @param amount        The amount of damage originally requested
         * @param breakCallback Callback when the stack reaches zero damage. See {@link ItemStack#hurtAndBreak(int, LivingEntity, Consumer)} (int, LivingEntity, Consumer)} and its callsites for more information.
         * @return The amount of damage to pass to vanilla's logic
         */
        int damage(ItemStack stack, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback);
    }
}
