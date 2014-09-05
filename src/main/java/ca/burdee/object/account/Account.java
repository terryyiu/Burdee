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

package ca.burdee.object.account;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.burdee.object.AbstractBurdeeListener;
import ca.burdee.object.AbstractBurdeeObject;
import ca.burdee.object.BurdeeListener;
import ca.burdee.object.BurdeeObject;
import ca.burdee.object.BurdeeRoot;
import ca.burdee.object.contact.Contact;
import ca.burdee.object.contact.ContactCategory;
import ca.burdee.object.contact.Conversation;

/**
 * This {@link Account} class represents an abstract instant messaging service
 * account, which has information about credentials, connect and disconnect from
 * a service, and have contacts / contact categories.
 */
public abstract class Account extends AbstractBurdeeObject {
	
	/**
	 * The {@link List} of {@link Contact}s that this {@link Account} can chat
	 * with.
	 */
	private List<Contact> contacts = new ArrayList<Contact>();

	/**
	 * The {@link List} of {@link ContactCategory}s that this {@link Account}'s
	 * {@link Contact}s can be contained under. Note that multiple
	 * {@link ContactCategory}s can contain the same {@link Contact}.
	 */
	private List<ContactCategory> categories = new ArrayList<ContactCategory>();

	/**
	 * The {@link List} of {@link Conversation}s that this {@link Account} is
	 * currently having.
	 */
	private List<Conversation> conversations = new ArrayList<Conversation>();

	/**
	 * @see #getPassword()
	 */
	private String password;
	
	/**
	 * @see #isConnectAutomatically()
	 */
	private boolean connectAutomatically;

	/**
	 * @see #isPopulated()
	 */
	protected boolean populated;

	/**
	 * This {@link BurdeeListener} populates this {@link Account} after a
	 * connection has been established.
	 */
	private final BurdeeListener connectionListener = new AbstractBurdeeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("connection")) {
				((Account) evt.getSource()).populate();
			}
		}
	};
	
	/**
	 * Creates a new {@link Account} with no username. Note that the username
	 * can be set later on through the {@link #setName(String)} method.
	 */
	public Account() {
		this(null);
	}

	/**
	 * Creates a new {@link Account} with a given username.
	 * 
	 * @param username
	 *            The username of the {@link Account}.
	 */
	public Account(String username) {
		super();
		setName(username);
		connectAutomatically = false;
		populated = false;
		attachConnectionListener();
	}
	
	@Override
	protected void addChildImpl(BurdeeObject child, int index) {
		if (child instanceof Contact) {
			contacts.add(index, (Contact) child);
		} else if (child instanceof ContactCategory) {
			categories.add(index, (ContactCategory) child);
		} else if (child instanceof Conversation) {
			conversations.add(index, (Conversation) child);
		}
	}
	
	@Override
	protected boolean removeChildImpl(BurdeeObject child) {
		if (child instanceof Contact) {
			return contacts.remove(child);
		} else if (child instanceof ContactCategory) {
			return categories.remove(child);
		} else if (child instanceof Conversation) {
			return conversations.remove(child);
		}
		return false;
	}

	@Override
	public List<Class<? extends BurdeeObject>> getAllowedChildTypes() {
		List<Class<? extends BurdeeObject>> types = new ArrayList<Class<? extends BurdeeObject>>();
		types.add(Contact.class);
		types.add(ContactCategory.class);
		types.add(Conversation.class);
		return Collections.unmodifiableList(types);
	}

	@Override
	public List<? extends BurdeeObject> getChildren() {
		List<BurdeeObject> children = new ArrayList<BurdeeObject>();
		children.addAll(contacts);
		children.addAll(categories);
		children.addAll(conversations);
		return Collections.unmodifiableList(children);
	}
	
	@Override
	public BurdeeRoot getParent() {
		return (BurdeeRoot) super.getParent();
	}
	
	@Override
	public void setParent(BurdeeObject parent) {
		if (parent != null && !(parent instanceof BurdeeRoot)) {
			throw new IllegalArgumentException(Account.class.getSimpleName() + 
					" can only have parent of type " + 
					BurdeeRoot.class.getSimpleName() + ".");
		}
		super.setParent(parent);
	}

	/**
	 * Gets the plain-text password for this {@link Account}.
	 * 
	 * @return The {@link String} plain-text password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the plain-text password for this {@link Account}.
	 * 
	 * @param password
	 *            The {@link String} plain-text password.
	 */
	public void setPassword(String password) {
		String oldPassword = this.password;
		this.password = password;
		firePropertyChanged("password", oldPassword, password);
	}

	/**
	 * Gets the flag that determines whether this {@link Account} should connect
	 * automatically on startup.
	 * 
	 * @return true if this {@link Account} should connect automatically on
	 *         startup.
	 */
	public boolean isConnectAutomatically() {
		return connectAutomatically;
	}

	/**
	 * Sets the flag that determines whether this {@link Account} should connect
	 * automatically on startup.
	 * 
	 * @param connectAutomatically
	 *            true if this {@link Account} should connect automatically on
	 *            startup.
	 */
	public void setConnectAutomatically(boolean connectAutomatically) {
		boolean oldConnectAutomatically = this.connectAutomatically;
		this.connectAutomatically = connectAutomatically;
		firePropertyChanged("connectAutomatically", oldConnectAutomatically, connectAutomatically);
	}

	/**
	 * Gets the boolean flag that determines whether this {@link Account}'s
	 * {@link List} of {@link Contact}s have been populated.
	 * 
	 * @return true if this {@link Account} has been populated.
	 */
	public boolean isPopulated() {
		return populated;
	}

	/**
	 * Connects to this {@link Account}'s IM service.
	 * 
	 * @throws FailedConnectionException
	 *             Thrown if the connection attempt was unsuccessful.
	 */
	public abstract void connect() throws FailedConnectionException;
	
	/**
	 * Disconnects from this {@link Account}'s IM service.
	 */
	public abstract void disconnect();

	/**
	 * Determines if this {@link Account} is connected to its IM service.
	 * 
	 * @return true if it is connected.
	 */
	public abstract boolean isConnected();

	/**
	 * Populates the {@link Contact}s and {@link ContactCategory}s.
	 */
	public abstract void populate();

	/**
	 * Updates all of the properties from another {@link Account} object except
	 * UUID. Note that extending Account classes should override this method
	 * because there may be more properties in the non-abstract {@link Account}
	 * that needs to be copied. The only 3 properties that are copied in the
	 * abstract {@link Account#updateToMatch(Account)} method are name,
	 * password, and connectAutomatically.
	 * 
	 * @param account
	 *            The {@link Account} properties to copy into this
	 *            {@link Account} object.
	 */
	public void updateToMatch(Account account) {
		setName(account.getName());
		setPassword(account.getPassword());
		setConnectAutomatically(account.isConnectAutomatically());
	}

	/**
	 * Finds the first matching {@link ContactCategory} under this
	 * {@link Account} that matches a given name.
	 * 
	 * @param name
	 *            The name of the {@link ContactCategory} to search for.
	 * @return The matching {@link ContactCategory}.
	 */
	public ContactCategory findContactCategoryByName(String name) {
		for (ContactCategory category : categories) {
			if (category.getName().equals(name)) {
				return category;
			}
		}
		return null;
	}

	/**
	 * Attaches the {@link BurdeeListener} that listens to the connection
	 * property change and populates the {@link List} of {@link Contact}s on the
	 * event.
	 */
	public void attachConnectionListener() {
		addBurdeeListener(connectionListener);
	}

	/**
	 * Detaches the {@link BurdeeListener} that listens to the connection
	 * property change and populates the {@link List} of {@link Contact}s on the
	 * event.
	 */
	public void detachConnectionListener() {
		removeBurdeeListener(connectionListener);
	}
	
	@Override
	public String toString() {
		return getName();
	}

	/**
	 * Creates a new {@link Conversation} and adds it as a child of this
	 * {@link Account}.
	 * 
	 * @param contacts
	 *            The {@link List} of {@link Contact}s that are participating in
	 *            the created {@link Conversation}.
	 * @return The new {@link Conversation}.
	 */
	public abstract Conversation createConversation(List<Contact> contacts);

}
