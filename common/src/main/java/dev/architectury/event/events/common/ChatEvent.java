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

package dev.architectury.event.events.common;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.TextFilter;

public interface ChatEvent {
    /**
     * @see Server#process(ServerPlayer, TextFilter.FilteredText, ChatComponent)
     */
    Event<Server> SERVER = EventFactory.createEventResult();
    
    interface Server {
        /**
         * Invoked when the server receives a message from a client.
         * Equivalent to Forge's {@code ServerChatEvent} event.
         *
         * @param player    The player who has sent the message.
         * @param message   The raw message itself.
         * @param component The message as component.
         * @return A {@link EventResult} determining the outcome of the event,
         * the execution of the vanilla message may be cancelled by the result.
         */
        EventResult process(ServerPlayer player, TextFilter.FilteredText message, ChatComponent component);
    }
    
    interface ChatComponent {
        Component getRaw();
        
        Component getFiltered();
        
        void setRaw(Component raw);
        
        void setFiltered(Component filtered);
    }
}
