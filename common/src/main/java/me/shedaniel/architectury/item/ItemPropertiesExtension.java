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

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

import me.shedaniel.architectury.registry.ToolType;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * A common interface for {@link Item.Properties} for both the fabric and forge environments. Deprecated methods in this class only work on one environment, so do not necessarily expect the same functionality.
 */
public interface ItemPropertiesExtension {

    /**
     * Only works in forge environment
     *
     * @return this
     */
    @Deprecated
    ItemPropertiesExtension cannotRepair();

    /**
     * Creates a custom item renderer. In fabric, it is the same as calling {@code BuiltinItemRendererRegistry.INSTANCE.register(ITEM, new CustomItemRenderer());}
     *
     * @param renderer The Item renderer, wrapped in a suppiler and then a callable to prevent loading client code on a server
     * @return this
     */
    @Deprecated
    ItemPropertiesExtension withCustomRenderer(Supplier<Callable<BlockEntityWithoutLevelRenderer>> renderer);

    /**
     * Only works in forge environment. For fabric add it to the {@code fabric:<tool_type>} tag
     *
     * @param type  The tool type
     * @param level The mining level
     * @return this
     */
    @Deprecated
    ItemPropertiesExtension asToolType(ToolType type, int level);

    /**
     * Only works in fabric environment
     *
     * @param function A function to convert an item stack to a Equipment Slot
     * @return this
     */
    @Deprecated
    ItemPropertiesExtension withEquipmentSlot(Function<ItemStack, EquipmentSlot> function);

    /**
     * Only works in fabric environment
     *
     * @param handler The {@link ArchitecturyItemProperties.CustomDamageHandler}
     * @return this
     */
    ItemPropertiesExtension withCustomDamageHandler(ArchitecturyItemProperties.CustomDamageHandler handler);
}
