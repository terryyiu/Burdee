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

/**
 * This enumeration defines the different IM protocols that extend from the
 * {@link Account} class. The user will be able to add {@link Account}s of all
 * these types.
 */
public enum Protocol {
	XMPP (XMPPAccount.class, "XMPP"),
	GOOGLE_TALK (GoogleTalkAccount.class, "Google Talk");

	/**
	 * The {@link Account} class associated with this {@link Protocol}.
	 */
	private final Class<? extends Account> accountClass;
	
	/**
	 * The user friendly name of this {@link Protocol}.
	 */
	private final String name;

	/**
	 * Creates a new {@link Protocol}.
	 * 
	 * @param accountClass
	 *            The {@link Account} class associated with this {@link Protocol}.
	 * @param name
	 *            The user friendly name of this {@link Protocol}.
	 */
	Protocol(Class<? extends Account> accountClass, String name) {
		this.accountClass = accountClass;
		this.name = name;
	}

	/**
	 * Gets the {@link Account} class associated with this {@link Protocol}.
	 * 
	 * @return The {@link Account} class.
	 */
	public Class<? extends Account> getAccountClass() {
		return accountClass;
	}

	/**
	 * Gets the user friendly name of this {@link Protocol}.
	 * 
	 * @return The user friendly protocol name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Finds the corresponding {@link Protocol} given an {@link Account} class.
	 * 
	 * @param accountClass
	 *            The {@link Account} class to find.
	 * @return The matching {@link Protocol} or null if not found.
	 */
	public static Protocol findByAccountClass(Class<? extends Account> accountClass) {
		for (Protocol protocol : values()) {
			if (protocol.getAccountClass() == accountClass) {
				return protocol;
			}
		}
		return null;
	}
}
