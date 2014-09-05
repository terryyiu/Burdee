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

package ca.burdee.swing;

import java.awt.event.ItemListener;

import ca.burdee.object.account.Account;
import ca.burdee.object.account.StubAccount;
import ca.burdee.object.account.XMPPAccount;

/**
 * This {@link AccountPanelFactory} is a class that contains a static method for
 * creating {@link AccountPanel}s for {@link Account} specific types.
 */
public class AccountPanelFactory {

	/**
	 * This class is a factory class. Instances of this class should never be
	 * created.
	 */
	private AccountPanelFactory() {}

	/**
	 * Creates a new {@link AccountPanel} from a given {@link Account}.
	 * 
	 * @param <T>
	 *            The {@link Account} type to use for creating the
	 *            {@link AccountPanel} that is specific to that {@link Account}
	 *            type's properties.
	 * @param account
	 *            The {@link Account} that is modified by the returned
	 *            {@link AccountPanel}.
	 * @return The created {@link AccountPanel}.
	 */
	public static <T extends Account> AccountPanel<T> createAccountPanel(T account, ItemListener protocolListener) {
		if (XMPPAccount.class.isAssignableFrom(account.getClass())) {
			return (AccountPanel<T>) new XMPPAccountPanel<XMPPAccount>((XMPPAccount) account, protocolListener);
		} else if (account instanceof StubAccount) {
			return (AccountPanel<T>) new StubAccountPanel((StubAccount) account, protocolListener);
		} else {
			throw new IllegalArgumentException(
					"Cannot create an account panel for type " + account.getClass());
		}
	}

}
