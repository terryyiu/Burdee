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

import java.util.Collections;
import java.util.List;

import ca.burdee.object.AbstractBurdeeObject;
import ca.burdee.object.BurdeeObject;
import ca.burdee.object.account.Account;

/**
 * This {@link BurdeeObject} represents an individual {@link Contact} which an
 * {@link Account} can chat with.
 */
public class Contact extends AbstractBurdeeObject {
	
	/**
	 * @see #getAddress()
	 */
	private String address;
	
	/**
	 * @see #getStatus()
	 */
	private Status status;

	/**
	 * Creates a new {@link Contact}.
	 * 
	 * @param name
	 *            The name of the contact.
	 */
	public Contact(String name, String address, Status status) {
		super();
		setName(name);
		setAddress(address);
		setStatus(status);
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
			throw new IllegalArgumentException(Contact.class.getSimpleName() +
					" can only have parent of type " + Account.class.getSimpleName() + ".");
		}
		super.setParent(parent);
	}

	/**
	 * Gets the address of this {@link Contact}, usually in the form of
	 * userid@domain.
	 * 
	 * @return The address of this {@link Contact}.
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address of this {@link Contact}, usually in the form of
	 * userid@domain.
	 * 
	 * @param address
	 *            The address of this {@link Contact}.
	 */
	public void setAddress(String address) {
		String oldAddress = this.address;
		this.address = address;
		firePropertyChanged("address", oldAddress, address);
	}

	/**
	 * Gets this {@link Contact}'s online status.
	 * 
	 * @return The {@link Status} of this {@link Contact}.
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * Sets this {@link Contact}'s online status.
	 * 
	 * @param status
	 *            The {@link Status} to set for this {@link Contact}.
	 */
	public void setStatus(Status status) {
		Status oldStatus = this.status;
		this.status = status;
		firePropertyChanged("status", oldStatus, status);
	}
	
	@Override
	public String toString() {
		return getName() + " (" + address + ") " + status;
	}

}
