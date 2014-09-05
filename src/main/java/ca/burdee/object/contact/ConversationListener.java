/*
 * Burdee - an instant messaging client
 * Copyright (C) 2010  Terry Yiu
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.burdee.object.contact;

import java.util.EventListener;

/**
 * This {@link EventListener} listens to messages that are sent and received
 * within {@link Conversation}s.
 */
public interface ConversationListener extends EventListener {

	/**
	 * This event is fired when a message is received from a
	 * {@link Conversation}.
	 * 
	 * @param conversation
	 *            The {@link Conversation} the message came from.
	 * @param contact
	 *            The {@link Contact} that sent the message.
	 * @param message
	 *            The message that was received.
	 */
	void messageReceived(Conversation conversation, Contact contact, String message);

	/**
	 * This event is fired when a message is sent to a {@link Conversation}.
	 * 
	 * @param conversation
	 *            The {@link Conversation} that the message is being sent to.
	 * @param message
	 *            The message that was sent.
	 */
	void messageSent(Conversation conversation, String message);

}
