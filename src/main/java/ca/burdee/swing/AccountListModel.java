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

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;
import javax.swing.SwingWorker;

import ca.burdee.object.AbstractBurdeeListener;
import ca.burdee.object.BurdeeListener;
import ca.burdee.object.BurdeeRoot;
import ca.burdee.object.BurdeeObjectUUIDComparator;
import ca.burdee.object.account.Account;

/**
 * This {@link ListModel} represents all of the {@link Account}s that is to be
 * created, removed, modified, or already exist in the system. Actual existing
 * {@link Account}s are not modified directly. Deep copies of the existing
 * {@link Account}s are created before any changes are made.
 */
public class AccountListModel extends AbstractListModel {

	/**
	 * The {@link BurdeeRoot} object to retrieve the current {@link Account}s
	 * from.
	 */
	private final BurdeeRoot root;

	/**
	 * The {@link List} of {@link Account}s that this model is modifying. This
	 * {@link List} contains copies of the child {@link Account}s under the
	 * {@link BurdeeRoot} so that they are not modified directly. Only when
	 * {@link #applyChanges()} is called, the changes are synchronized back with
	 * the {@link BurdeeRoot} object.
	 */
	private final List<Account> accounts = new ArrayList<Account>();

	/**
	 * This {@link BurdeeListener} listens to changes of an {@link Account}'s
	 * name and notifies that the list model has changed.
	 */
	private final BurdeeListener accountListener = new AbstractBurdeeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("name") && getAccounts().contains(evt.getSource())) {
				int index = getAccounts().indexOf(evt.getSource());
				fireContentsChanged(AccountListModel.this, index, index);
			}
		}
	};

	/**
	 * Creates a new {@link AccountListModel}.
	 * 
	 * @param root
	 *            The {@link BurdeeRoot} object to retrieve the {@link List} of
	 *            child {@link Account} objects from.
	 */
	public AccountListModel(BurdeeRoot root) {
		this.root = root;
		try {
			for (Account account : root.getChildren(Account.class)) {
				Account accountCopy;
				accountCopy = account.getClass().newInstance();
				accountCopy.updateToMatch(account);
				accountCopy.addBurdeeListener(accountListener);
				accounts.add(accountCopy);
			}
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getSize() {
		return accounts.size();
	}

	@Override
	public Account getElementAt(int index) {
		return accounts.get(index);
	}

	/**
	 * Applies the changes made to all of the {@link Account}s by synchronizing
	 * with the {@link BurdeeRoot}.
	 */
	public void applyChanges() {
		SwingWorker<Boolean, Boolean> worker = new SwingWorker<Boolean, Boolean>() {

			@Override
			protected Boolean doInBackground() throws Exception {
				if (hasUnsavedChanges()) {
					List<Account> source = new ArrayList<Account>(accounts);
					List<Account> target = new ArrayList<Account>(root.getChildren(Account.class));

					Comparator<Account> comparator = new BurdeeObjectUUIDComparator<Account>();
					Collections.sort(source, comparator);
					Collections.sort(target, comparator);
					
					for (int i = 0, j = 0; i < source.size() || j < target.size();) {
						Account sourceAccount = null;
						Account targetAccount = null;
						int compare = 0;
						
						if (i < source.size()) {
							sourceAccount = source.get(i);
						} else {
							compare = 1;
						}
						
						if (j < target.size()) {
							targetAccount = target.get(j);
						} else {
							compare = -1;
						}
						
						if (compare == 0) {
							compare = comparator.compare(sourceAccount, targetAccount);
						}
						
						if (compare < 0) {
							Account accountCopy = sourceAccount.getClass().newInstance();
							accountCopy.updateToMatch(sourceAccount);
							accountCopy.attachConnectionListener();
							root.addChild(accountCopy);
							i++;
						} else if (compare > 0) {
							root.removeChild(targetAccount);
							j++;
						} else {
							targetAccount.updateToMatch(sourceAccount);
							i++;
							j++;
						}
					}
				}
				return true;
			}
			
		};
		
		worker.execute();
	}

	/**
	 * Determines if there are any differences between the {@link List} of
	 * {@link Account}s this model is using and the {@link List} of actual
	 * {@link Account}s under the {@link BurdeeRoot} object.
	 * 
	 * @return true if there are any differences between this model and the
	 *         {@link BurdeeRoot} object.
	 */
	public boolean hasUnsavedChanges() {
		List<Account> target = new ArrayList<Account>(root.getChildren(Account.class));
		if (accounts.size() != target.size()) {
			return true;
		}
		
		List<Account> source = new ArrayList<Account>(accounts);
		Comparator<Account> comparator = new BurdeeObjectUUIDComparator<Account>();
		Collections.sort(source, comparator);
		Collections.sort(target, comparator);
		
		for (int i = 0; i < source.size(); i++) {
			Account sourceAccount = source.get(i);
			Account targetAccount = target.get(i);
			int compare = comparator.compare(sourceAccount, targetAccount);
			if (compare != 0) {
				return true;
			} else if (!sourceAccount.equals(targetAccount)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Gets the unmodifiable {@link List} of {@link Account}s this model is
	 * using.
	 * 
	 * @return The unmodifiable {@link List} of {@link Account}s.
	 */
	public List<Account> getAccounts() {
		return Collections.unmodifiableList(accounts);
	}

	/**
	 * Adds a new {@link Account} to the end of this model.
	 * 
	 * @param account
	 *            The {@link Account} to add.
	 */
	public void addElement(Account account) {
		addElement(account, getSize());
	}

	/**
	 * Adds a new {@link Account} to the specified index of this model.
	 * 
	 * @param account
	 *            The {@link Account} to add.
	 * @param index
	 *            The index in the model to add the {@link Account} to.
	 */
	public void addElement(Account account, int index) {
		account.addBurdeeListener(accountListener);
		accounts.add(index, account);
		fireIntervalAdded(this, index, index);
	}

	/**
	 * Removes an {@link Account} from this model.
	 * 
	 * @param index
	 *            The index of the {@link Account} to remove.
	 */
	public void removeElementAt(int index) {
		Account account = accounts.remove(index);
		account.removeBurdeeListener(accountListener);
		fireIntervalRemoved(this, index, index);
	}
	
}
