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

package me.shedaniel.architectury.test;

import com.mojang.blaze3d.platform.InputConstants;
import me.shedaniel.architectury.event.events.*;
import me.shedaniel.architectury.event.events.client.ClientChatEvent;
import me.shedaniel.architectury.event.events.client.ClientScreenInputEvent;
import me.shedaniel.architectury.event.events.client.ClientLifecycleEvent;
import me.shedaniel.architectury.event.events.client.ClientPlayerEvent;
import me.shedaniel.architectury.hooks.ExplosionHooks;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.test.client.ClientOverlayMessageSink;
import me.shedaniel.architectury.utils.Env;
import me.shedaniel.architectury.utils.EnvExecutor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class TestMod {
    public static final MessageSink SINK = EnvExecutor.getEnvSpecific(() -> ClientOverlayMessageSink::new, () -> ConsoleMessageSink::new);
    
    public static void initialize() {
        debugEvents();
        if (Platform.getEnvironment() == Env.CLIENT)
            debugEventsClient();
    }
    
    public static void debugEvents() {
        ChatEvent.SERVER.register((player, message, component) -> {
            SINK.accept("Server chat received: " + message);
            return InteractionResultHolder.pass(component);
        });
        CommandPerformEvent.EVENT.register(event -> {
            SINK.accept("Server command performed: " + event.getResults().getReader().getString());
            return InteractionResult.PASS;
        });
        CommandRegistrationEvent.EVENT.register((dispatcher, selection) -> {
            SINK.accept("Server commands registers");
        });
        EntityEvent.LIVING_DEATH.register((entity, source) -> {
            if (entity instanceof Player) {
                SINK.accept(entity.getScoreboardName() + " died to " + source.getMsgId() + logSide(entity.level));
            }
            return InteractionResult.PASS;
        });
        EntityEvent.LIVING_ATTACK.register((entity, source, amount) -> {
            if (source.getDirectEntity() instanceof Player) {
                SINK.accept(source.getDirectEntity().getScoreboardName() + " deals %.2f damage" + logSide(entity.level), amount);
            }
            return InteractionResult.PASS;
        });
        EntityEvent.ADD.register((entity, level) -> {
            if (entity instanceof Player) {
                SINK.accept(entity.getScoreboardName() + " was added to " + level.dimension().location().toString() + logSide(level));
            }
            return InteractionResult.PASS;
        });
        EntityEvent.PLACE_BLOCK.register((world, pos, state, placer) -> {
            SINK.accept(Optional.ofNullable(placer).map(Entity::getScoreboardName).orElse("null") + " places block at " + toShortString(pos) + logSide(world));
            return InteractionResult.PASS;
        });
        ExplosionEvent.DETONATE.register((world, explosion, affectedEntities) -> {
            SINK.accept(world.dimension().location() + " explodes at " + toShortString(ExplosionHooks.getPosition(explosion)) + logSide(world));
        });
        InteractionEvent.LEFT_CLICK_BLOCK.register((player, hand, pos, face) -> {
            SINK.accept(player.getScoreboardName() + " left clicks " + toShortString(pos) + logSide(player.level));
            return InteractionResult.PASS;
        });
        InteractionEvent.RIGHT_CLICK_BLOCK.register((player, hand, pos, face) -> {
            SINK.accept(player.getScoreboardName() + " right clicks " + toShortString(pos) + logSide(player.level));
            return InteractionResult.PASS;
        });
        InteractionEvent.RIGHT_CLICK_ITEM.register((player, hand) -> {
            SINK.accept(player.getScoreboardName() + " uses " + (hand == InteractionHand.MAIN_HAND ? "main hand" : "off hand") + logSide(player.level));
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        });
        InteractionEvent.INTERACT_ENTITY.register((player, entity, hand) -> {
            SINK.accept(player.getScoreboardName() + " interacts with " + entity.getScoreboardName() + " using " + (hand == InteractionHand.MAIN_HAND ? "main hand" : "off hand") + logSide(player.level));
            return InteractionResult.PASS;
        });
        LifecycleEvent.SERVER_BEFORE_START.register(instance -> {
            SINK.accept("Server ready to start");
        });
        LifecycleEvent.SERVER_STARTING.register(instance -> {
            SINK.accept("Server starting");
        });
        LifecycleEvent.SERVER_STARTED.register(instance -> {
            SINK.accept("Server started");
        });
        LifecycleEvent.SERVER_STOPPING.register(instance -> {
            SINK.accept("Server stopping");
        });
        LifecycleEvent.SERVER_STOPPED.register(instance -> {
            SINK.accept("Server stopped");
        });
        LifecycleEvent.SERVER_WORLD_LOAD.register(instance -> {
            SINK.accept("Server world loaded: " + instance.dimension().location());
        });
        LifecycleEvent.SERVER_WORLD_UNLOAD.register(instance -> {
            SINK.accept("Server world unloaded: " + instance.dimension().location());
        });
        LifecycleEvent.SERVER_WORLD_SAVE.register(instance -> {
            SINK.accept("Server world saved: " + instance.dimension().location());
        });
        PlayerEvent.PLAYER_JOIN.register(player -> {
            SINK.accept(player.getScoreboardName() + " joined" + logSide(player.level));
        });
        PlayerEvent.PLAYER_QUIT.register(player -> {
            SINK.accept(player.getScoreboardName() + " quit" + logSide(player.level));
        });
        PlayerEvent.PLAYER_RESPAWN.register((player, conqueredEnd) -> {
            if (!conqueredEnd) {
                SINK.accept(player.getScoreboardName() + " respawns " + logSide(player.level));
            }
        });
        PlayerEvent.PLAYER_CLONE.register((oldPlayer, newPlayer, wonGame) -> {
            SINK.accept("Player cloned: " + newPlayer.getScoreboardName() + logSide(newPlayer.level));
        });
        PlayerEvent.PLAYER_ADVANCEMENT.register((player, advancement) -> {
            SINK.accept(player.getScoreboardName() + " was awarded with %s" + logSide(player.level), advancement.getChatComponent().getString());
        });
        PlayerEvent.CRAFT_ITEM.register((player, constructed, inventory) -> {
            SINK.accept(player.getScoreboardName() + " crafts " + new TranslatableComponent(constructed.getDescriptionId()).getString() + logSide(player.level));
        });
        PlayerEvent.SMELT_ITEM.register((player, smelted) -> {
            SINK.accept(player.getScoreboardName() + " smelts " + new TranslatableComponent(smelted.getDescriptionId()).getString() + logSide(player.level));
        });
        PlayerEvent.PICKUP_ITEM_POST.register((player, entity, stack) -> {
            SINK.accept(player.getScoreboardName() + " picks up " + new TranslatableComponent(stack.getDescriptionId()).getString() + logSide(player.level));
        });
        PlayerEvent.DROP_ITEM.register((player, entity) -> {
            SINK.accept(player.getScoreboardName() + " drops " + new TranslatableComponent(entity.getItem().getDescriptionId()).getString() + logSide(player.level));
            return InteractionResult.PASS;
        });
        PlayerEvent.BREAK_BLOCK.register((world, pos, state, player, xp) -> {
            SINK.accept(player.getScoreboardName() + " breaks " + toShortString(pos) + logSide(player.level));
            return InteractionResult.PASS;
        });
        PlayerEvent.OPEN_MENU.register((player, menu) -> {
            SINK.accept(player.getScoreboardName() + " opens " + toSimpleName(menu) + logSide(player.level));
        });
        PlayerEvent.CLOSE_MENU.register((player, menu) -> {
            SINK.accept(player.getScoreboardName() + " closes " + toSimpleName(menu) + logSide(player.level));
        });
    }
    
    public static String toShortString(Vec3i pos) {
        return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
    }
    
    public static String toShortString(Position pos) {
        return pos.x() + ", " + pos.y() + ", " + pos.z();
    }
    
    public static String logSide(Level level) {
        if (level.isClientSide())
            return " (client)";
        return " (server)";
    }
    
    @Environment(EnvType.CLIENT)
    public static void debugEventsClient() {
        ClientChatEvent.CLIENT.register(message -> {
            SINK.accept("Client chat sent: " + message);
            return InteractionResultHolder.pass(message);
        });
        ClientChatEvent.CLIENT_RECEIVED.register((type, message, sender) -> {
            SINK.accept("Client chat received: " + message.getString());
            return InteractionResultHolder.pass(message);
        });
        ClientLifecycleEvent.CLIENT_WORLD_LOAD.register(world -> {
            SINK.accept("Client world loaded: " + world.dimension().location().toString());
        });
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(player -> {
            SINK.accept(player.getScoreboardName() + " joined (client)");
        });
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(player -> {
            SINK.accept(player.getScoreboardName() + " quit (client)");
        });
        ClientPlayerEvent.CLIENT_PLAYER_RESPAWN.register((oldPlayer, newPlayer) -> {
            SINK.accept(newPlayer.getScoreboardName() + " respawned (client)");
        });
        GuiEvent.INIT_PRE.register((screen, widgets, children) -> {
            SINK.accept(toSimpleName(screen) + " initializes");
            return InteractionResult.PASS;
        });
        InteractionEvent.CLIENT_LEFT_CLICK_AIR.register((player, hand) -> {
            SINK.accept(player.getScoreboardName() + " left clicks air" + logSide(player.level));
        });
        InteractionEvent.CLIENT_RIGHT_CLICK_AIR.register((player, hand) -> {
            SINK.accept(player.getScoreboardName() + " right clicks air" + logSide(player.level));
        });
        RecipeUpdateEvent.EVENT.register(recipeManager -> {
            SINK.accept("Client recipes received");
        });
        TextureStitchEvent.POST.register(atlas -> {
            SINK.accept("Client texture stitched: " + atlas.location());
        });
        ClientScreenInputEvent.MOUSE_SCROLLED_PRE.register((client, screen, mouseX, mouseY, amount) -> {
            SINK.accept("Screen Mouse amount: %.2f distance", amount);
            return InteractionResult.PASS;
        });
        ClientScreenInputEvent.MOUSE_CLICKED_PRE.register((client, screen, mouseX, mouseY, button) -> {
            SINK.accept("Screen Mouse clicked: " + button);
            return InteractionResult.PASS;
        });
        ClientScreenInputEvent.MOUSE_RELEASED_PRE.register((client, screen, mouseX, mouseY, button) -> {
            SINK.accept("Screen Mouse released: " + button);
            return InteractionResult.PASS;
        });
        ClientScreenInputEvent.MOUSE_DRAGGED_PRE.register((client, screen, mouseX1, mouseY1, button, mouseX2, mouseY2) -> {
            SINK.accept("Screen Mouse dragged: %d (%d,%d) by (%d,%d)", button, (int) mouseX1, (int) mouseY1, (int) mouseX2, (int) mouseY2);
            return InteractionResult.PASS;
        });
        ClientScreenInputEvent.CHAR_TYPED_PRE.register((client, screen, character, keyCode) -> {
            SINK.accept("Screen Char typed: " + character);
            return InteractionResult.PASS;
        });
        ClientScreenInputEvent.KEY_PRESSED_PRE.register((client, screen, keyCode, scanCode, modifiers) -> {
            SINK.accept("Screen Key pressed: " + InputConstants.getKey(keyCode, scanCode).getDisplayName().getString());
            return InteractionResult.PASS;
        });
        ClientScreenInputEvent.KEY_RELEASED_PRE.register((client, screen, keyCode, scanCode, modifiers) -> {
            SINK.accept("Screen Key released: " + InputConstants.getKey(keyCode, scanCode).getDisplayName().getString());
            return InteractionResult.PASS;
        });
    }
    
    private static String toSimpleName(Object o) {
        return o.getClass().getSimpleName() + "@" + Integer.toHexString(o.hashCode());
    }
}
