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

package ca.burdee.object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.burdee.object.account.Account;
import ca.burdee.object.account.FailedConnectionException;

/**
 * The root {@link BurdeeObject} that contains {@link Account}s. This object is
 * the top level ancestor object of the hierarchy tree.
 */
public class BurdeeRoot extends AbstractBurdeeObject {
	
	/**
	 * The {@link List} of child {@link Account}s that are connected.
	 */
	private List<Account> accounts = new ArrayList<Account>();

	/**
	 * This {@link BurdeeListener} listens for added and removed child
	 * {@link Account}s from this {@link BurdeeRoot}. When an {@link Account} is
	 * added and its connect automatically property is set to true, this
	 * listener attempts to connect that {@link Account} to its IM service. When
	 * an {@link Account} is removed, this listener disconnects the
	 * {@link Account} from its IM service if it is still connected.
	 */
	private BurdeeListener accountListener = new AbstractBurdeeListener() {
		
		@Override
		public void childAdded(BurdeeChildEvent evt) {
			if (evt.getSource() == BurdeeRoot.this && 
					Account.class.isAssignableFrom(evt.getChild().getClass()) &&
					((Account) evt.getChild()).isConnectAutomatically()) {
				try {
					((Account) evt.getChild()).connect();
				} catch (FailedConnectionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public void childRemoved(BurdeeChildEvent evt) {
			if (evt.getSource() == BurdeeRoot.this &&
					Account.class.isAssignableFrom(evt.getChild().getClass())) {
				((Account) evt.getChild()).disconnect();
			}
		}
		
	};
	
	/**
	 * Creates a new {@link BurdeeRoot}.
	 */
	public BurdeeRoot() {
		super();
		addBurdeeListener(accountListener);
	}
	
	@Override
	protected void addChildImpl(BurdeeObject child, int index) {
		accounts.add((Account) child);
	}

	@Override
	protected boolean removeChildImpl(BurdeeObject child) {
		return accounts.remove(child);
	}

	@Override
	public List<Account> getChildren() {
		return Collections.unmodifiableList(accounts);
	}

	@Override
	public List<Class<? extends BurdeeObject>> getAllowedChildTypes() {
		return Collections.<Class<? extends BurdeeObject>>singletonList(Account.class);
	}
	
}
