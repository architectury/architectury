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

package me.shedaniel.architectury.impl.access;

import me.shedaniel.architectury.core.access.combined.CombinedAccess;
import me.shedaniel.architectury.core.access.specific.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

public class CombinedAccessImpl<T> implements CombinedAccess<T> {
    private final ProvidedAccessPoint<T, ?> provided;
    private final BlockAccessPoint<T, ?> block;
    private final ChunkAccessPoint<T, ?> chunk;
    private final LevelAccessPoint<T, ?> level;
    private final EntityAccessPoint<T, ?> entity;
    private final ItemAccessPoint<T, ?> item;
    
    public CombinedAccessImpl() {
        this.provided = ProvidedAccessPoint.create();
        this.block = BlockAccessPoint.create();
        this.chunk = ChunkAccessPoint.create();
        this.level = LevelAccessPoint.create();
        this.entity = EntityAccessPoint.create();
        this.item = ItemAccessPoint.create();
    }
    
    @Override
    public ProvidedAccessPoint<T, ?> general() {
        return provided;
    }
    
    @Override
    public BlockAccessPoint<T, ?> block() {
        return block;
    }
    
    @Override
    public ChunkAccessPoint<T, ?> chunk() {
        return chunk;
    }
    
    @Override
    public LevelAccessPoint<T, ?> level() {
        return level;
    }
    
    @Override
    public EntityAccessPoint<T, ?> entity() {
        return entity;
    }
    
    @Override
    public ItemAccessPoint<T, ?> item() {
        return item;
    }
    
    @Override
    public T get(Object o) {
        return provided.get(o);
    }
    
    @Override
    public T getByBlock(Level level, LevelChunk chunk, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        T t = this.block.getByBlock(level, chunk, pos, state, entity, direction);
        if (t != null) return t;
        return get(chunk);
    }
    
    @Override
    public T getByChunk(LevelChunk chunk) {
        T t = this.chunk.getByChunk(chunk);
        if (t != null) return t;
        return get(chunk);
    }
    
    @Override
    public T getByLevel(Level level) {
        T t = this.level.getByLevel(level);
        if (t != null) return t;
        return get(level);
    }
    
    @Override
    public T getByEntity(Entity entity) {
        T t = this.entity.getByEntity(entity);
        if (t != null) return t;
        return get(entity);
    }
    
    @Override
    public T getByItem(ItemStack stack) {
        T t = this.item.getByItem(stack);
        if (t != null) return t;
        return get(stack);
    }
}
