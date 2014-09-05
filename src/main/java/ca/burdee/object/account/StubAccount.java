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

import java.util.List;

import ca.burdee.object.contact.Contact;
import ca.burdee.object.contact.Conversation;

/**
 * This {@link Account} is a stub class that implement no operation in the
 * abstract methods defined in {@link Account}. It only contains the properties
 * defined in the {@link Account} abstract class. The purpose of this class is
 * to provide a protocol-less {@link Account} type that only stores the
 * username, password, and automatically connect properties.
 */
public class StubAccount extends Account {

	/**
	 * Creates a new {@link StubAccount}.
	 */
	public StubAccount() {
		super();
	}

	@Override
	public void connect() throws FailedConnectionException {
		// No operation.
	}

	@Override
	public void disconnect() {
		// No operation.
	}

	@Override
	public boolean isConnected() {
		return false;
	}

	@Override
	public void populate() {
		// No operation.
	}

	@Override
	public Conversation createConversation(List<Contact> contacts) {
		return null;
	}

}
