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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.burdee.object.AbstractBurdeeObject;
import ca.burdee.object.BurdeeObject;
import ca.burdee.object.account.Account;

/**
 * This {@link BurdeeObject} represents a single conversation between the user
 * and one or more {@link Contact}s.
 */
public abstract class Conversation extends AbstractBurdeeObject {

	/**
	 * The {@link List} of {@link Contact}s that this {@link Conversation}
	 * includes.
	 */
	private final List<Contact> contacts;

	/**
	 * The {@link List} of {@link ConversationListener}s that are notified of
	 * message sent or received events for this {@link Conversation}.
	 */
	private final List<ConversationListener> conversationListeners = 
		new ArrayList<ConversationListener>();

	/**
	 * Creates a new {@link Conversation} with a single {@link Contact}.
	 * 
	 * @param contact
	 *            The {@link Contact} to start the {@link Conversation} with.
	 */
	public Conversation(Contact contact) {
		this(Collections.singletonList(contact));
	}

	/**
	 * Creates a new {@link Conversation} with multiple {@link Contact}s.
	 * 
	 * @param contacts
	 *            The {@link List} of {@link Contact}s to start the
	 *            {@link Conversation} with.
	 */
	public Conversation(List<Contact> contacts) {
		super();
		this.contacts = new ArrayList<Contact>(contacts);
		
		StringBuilder sb = new StringBuilder();
		for (Contact c : contacts) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			if (c.getName() == null) {
				sb.append(c.getAddress());
			} else {
				sb.append(c.getName());
			}
		}
		setName(sb.toString());
	}

	@Override
	public List<? extends BurdeeObject> getChildren() {
		return Collections.emptyList();
	}

	@Override
	public List<Class<? extends BurdeeObject>> getAllowedChildTypes() {
		return Collections.emptyList();
	}

	@Override
	protected void addChildImpl(BurdeeObject child, int index) {
		// No operation.
	}

	@Override
	protected boolean removeChildImpl(BurdeeObject child) {
		return false;
	}
	
	@Override
	public Account getParent() {
		return (Account) super.getParent();
	}
	
	@Override
	public void setParent(BurdeeObject parent) {
		if (parent != null && !(Account.class.isAssignableFrom(parent.getClass()))) {
			throw new IllegalArgumentException(Conversation.class.getSimpleName() +
					" can only have parent of type " + Account.class.getSimpleName() + ".");
		}
		super.setParent(parent);
	}

	/**
	 * Gets the {@link List} of {@link Contact}s that are having this
	 * conversation with the parent {@link Account}.
	 * 
	 * @return The {@link List} of {@link Contact}s.
	 */
	public List<Contact> getContacts() {
		return Collections.unmodifiableList(contacts);
	}

	/**
	 * Adds a new {@link Contact} to the {@link Conversation} at the end of the
	 * {@link List} of all {@link Contact}s.
	 * 
	 * @param c
	 *            The {@link Contact} to add.
	 */
	public void addContact(Contact c) {
		addContact(c, contacts.size());
	}

	/**
	 * Adds a new {@link Contact} to the {@link Conversation} to the given index
	 * of the {@link List} of all {@link Contact}s.
	 * 
	 * @param c
	 *            The {@link Contact} to add.
	 * @param index
	 *            The index to add the {@link Contact} to.
	 */
	public void addContact(Contact c, int index) {
		if (c == null) {
			throw new IllegalArgumentException("Cannot add a null " + 
					Contact.class.getSimpleName() + " to the " + 
					Conversation.class.getSimpleName() + " " + getName() + ".");
		} else if (contacts.contains(c)) {
			throw new IllegalArgumentException("Cannot add " + 
					Contact.class.getSimpleName() + " to the " + 
					Conversation.class.getSimpleName() + " " + getName() + 
					" because it already exists.");
		}
		contacts.add(index, c);
	}

	/**
	 * Removes a {@link Contact} from this {@link Conversation}.
	 * 
	 * @param c
	 *            The {@link Contact} to remove.
	 * @return true if the removal was successful.
	 */
	public boolean removeContact(Contact c) {
		return contacts.remove(c);
	}

	/**
	 * Adds a {@link ConversationListener} to this {@link Conversation}.
	 * 
	 * @param l
	 *            The {@link ConversationListener} to add.
	 */
	public void addConversationListener(ConversationListener l) {
		synchronized(conversationListeners) {
			// Attempt to remove the ConversationListener first to prevent
			// it from being added twice.
			conversationListeners.remove(l);
			
			conversationListeners.add(l);
		}
	}

	/**
	 * Removes a {@link ConversationListener} to this {@link Conversation}.
	 * 
	 * @param l
	 *            The {@link ConversationListener} to remove.
	 */
	public void removeConversationListener(ConversationListener l) {
		synchronized(conversationListeners) {
			conversationListeners.remove(l);
		}
	}

	/**
	 * Fires a message received event to all the {@link ConversationListener}s
	 * that are listening to this {@link Conversation}.
	 * 
	 * @param contact
	 *            The {@link Contact} that sent the message.
	 * @param message
	 *            The message that was received.
	 */
	public void fireMessageReceived(Contact contact, String message) {
		synchronized(conversationListeners) {
			for (ConversationListener l : conversationListeners) {
				l.messageReceived(this, contact, message);
			}
		}
	}

	/**
	 * Fires a message sent event to all of the {@link ConversationListener}s
	 * that are listening to this {@link Conversation}.
	 * 
	 * @param message
	 *            The message that was sent.
	 */
	public void fireMessageSent(String message) {
		synchronized(conversationListeners) {
			for (ConversationListener l : conversationListeners) {
				l.messageSent(this, message);
			}
		}
	}

	/**
	 * Sends a message to all of the {@link Contact}s within this
	 * {@link Conversation}.
	 * 
	 * @param message
	 *            The message to send to the {@link Contact}s.
	 */
	public abstract void sendMessage(String message);
	
	@Override
	public String toString() {
		return getName();
	}

}
