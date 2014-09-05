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
 * This {@link BurdeeObject} is a contact list category which holds a collection
 * of {@link Contact}s.
 */
public class ContactCategory extends AbstractBurdeeObject {
	
	/**
	 * @see #getContacts()
	 */
	private final List<Contact> contacts = new ArrayList<Contact>();

	/**
	 * Creates a new {@link ContactCategory}.
	 * 
	 * @param name
	 *            The name of this category.
	 */
	public ContactCategory(String name) {
		super();
		setName(name);
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
	public List<? extends BurdeeObject> getChildren() {
		return Collections.emptyList();
	}

	@Override
	public List<Class<? extends BurdeeObject>> getAllowedChildTypes() {
		return Collections.emptyList();
	}
	
	@Override
	public Account getParent() {
		return (Account) super.getParent();
	}
	
	@Override
	public void setParent(BurdeeObject parent) {
		if (parent != null && !(parent instanceof Account)) {
			throw new IllegalArgumentException(ContactCategory.class.getSimpleName() +
					" can only have parent of type " + Account.class.getSimpleName() + ".");
		}
		super.setParent(parent);
	}

	/**
	 * Gets the {@link List} of {@link Contact}s which this
	 * {@link ContactCategory} contains. These are not children of this
	 * {@link ContactCategory}. They are simply contained within the
	 * {@link ContactCategory}, and multiple {@link ContactCategory}s can
	 * contain the same {@link Contact}.
	 * 
	 * @return The {@link List} of {@link Contact}s contained by this
	 *         {@link ContactCategory}.
	 */
	public List<Contact> getContacts() {
		return Collections.unmodifiableList(contacts);
	}

	/**
	 * Adds a {@link Contact} to this {@link ContactCategory}.
	 * 
	 * @param c
	 *            The {@link Contact} to add to this {@link ContactCategory}. If
	 *            the {@link Contact} already exists in this
	 *            {@link ContactCategory}'s {@link List} of contained
	 *            {@link Contact}s, no operating will occur. The {@link Contact}
	 *            will be added to the end of the {@link List} of
	 *            {@link Contact}s.
	 */
	public void addContact(Contact c) {
		addContact(c, contacts.size());
	}

	/**
	 * Adds a {@link Contact} to this {@link ContactCategory}.
	 * 
	 * @param c
	 *            The {@link Contact} to add to this {@link ContactCategory}. If
	 *            the {@link Contact} already exists in this
	 *            {@link ContactCategory}'s {@link List} of contained
	 *            {@link Contact}s, no operating will occur.
	 * @param index
	 *            The index of where the {@link Contact} is added in the
	 *            {@link List} of {@link Contact}s.
	 */
	public void addContact(Contact c, int index) {
		if (c == null) {
			throw new IllegalArgumentException("Cannot add a null " + 
					Contact.class.getSimpleName() + " to the " + 
					ContactCategory.class.getSimpleName() + " " + getName() + ".");
		} else if (contacts.contains(c)) {
			throw new IllegalArgumentException("Cannot add " + 
					Contact.class.getSimpleName() + " to the " + 
					ContactCategory.class.getSimpleName() + " " + getName() + 
					" because it already exists.");
		}
		contacts.add(index, c);
	}

	/**
	 * Removes a {@link Contact} from this {@link ContactCategory}.
	 * 
	 * @param c
	 *            The {@link Contact} to remove.
	 * @return true if the removal was successful.
	 */
	public boolean removeContact(Contact c) {
		return contacts.remove(c);
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
