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

package me.shedaniel.architectury.mixin.fabric;

import me.shedaniel.architectury.extensions.BlockEntityExtension;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockEntityExtension.class)
public interface MixinBlockEntityExtension extends BlockEntityClientSerializable {
    @Shadow(remap = false)
    @NotNull
    CompoundTag saveClientData(@NotNull CompoundTag tag);
    
    @Shadow(remap = false)
    void loadClientData(@NotNull BlockState pos, @NotNull CompoundTag tag);
    
    @Override
    default void fromClientTag(CompoundTag tag) {
        BlockEntity entity = (BlockEntity) this;
        if (entity.hasLevel()) {
            entity.setLevelAndPosition(entity.getLevel(), new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z")));
            loadClientData(entity.getBlockState(), tag);
        }
    }
    
    @Override
    default CompoundTag toClientTag(CompoundTag tag) {
        return saveClientData(tag);
    }
}
