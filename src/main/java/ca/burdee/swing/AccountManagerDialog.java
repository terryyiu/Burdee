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

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;
import ca.burdee.object.BurdeeRoot;
import ca.burdee.object.account.Account;
import ca.burdee.object.account.Protocol;
import ca.burdee.object.account.StubAccount;

/**
 * The {@link JDialog} that is used for creating, modifying, and deleting
 * instant messaging accounts.
 */
public class AccountManagerDialog extends JDialog {

	/**
	 * This {@link ImageIcon} is used for the add account {@link JButton} and
	 * {@link JMenuItem}.
	 */
	public static final ImageIcon ADD_ICON = 
		new ImageIcon(AccountManagerDialog.class.getResource("/icons/add.png"));

	/**
	 * This {@link ImageIcon} is used for the remove account {@link JButton} and
	 * {@link JMenuItem}.
	 */
	public static final ImageIcon REMOVE_ICON = 
		new ImageIcon(AccountManagerDialog.class.getResource("/icons/delete.png"));

	/**
	 * This {@link ImageIcon} is used as the dialog's icon.
	 */
	public static final ImageIcon ACCOUNT_MANAGER_ICON = 
		new ImageIcon(BurdeeFrame.class.getResource("/icons/group.png"));

	/**
	 * The {@link BurdeeRoot} object to retrieve account information from.
	 */
	private final BurdeeRoot root;

	/**
	 * The {@link JSplitPane} that splits the account {@link JList} and account
	 * {@link JPanel} components.
	 */
	private JSplitPane splitPane;

	/**
	 * The {@link JList} that is backed by an {@link AccountListModel}. Each
	 * element of this {@link JList} represents a single {@link Account}.
	 */
	private JList accountList;

	/**
	 * The {@link AccountListModel} that represents the list of all
	 * {@link Account}s.
	 */
	private AccountListModel accountListModel;

	/**
	 * The {@link AccountPanel} that displays information about the selected
	 * {@link Account} in the Account {@link JList}.
	 */
	private AccountPanel<?> accountPanel;

	/**
	 * The {@link Map} of {@link Account}s to its corresponding
	 * {@link AccountPanel}. This {@link Map} is useful in determining which
	 * {@link AccountPanel} to display for the selected {@link Account} in the
	 * {@link JList}.
	 */
	private Map<Account, AccountPanel<?>> accountPanelMap = 
		new HashMap<Account, AccountPanel<?>>();
	
	private ItemListener protocolListener = new ItemListener() {
		
		@Override
		public void itemStateChanged(ItemEvent evt) {
			if (accountList.isSelectionEmpty()) {
				throw new IllegalStateException(
						"Change in protocol occured while an account was not selected in the list.");
			}

			int index = accountList.getSelectedIndex();
			Account oldAccount = accountListModel.getElementAt(index);
			
			if (!(oldAccount instanceof StubAccount && accountPanel instanceof StubAccountPanel) && 
					accountPanelMap.remove(oldAccount) != accountPanel) {
				throw new IllegalStateException(
						"Inconsistent state: the panel of the selected account " +
						"is not the same as the current account panel.");
			}
			
			Account newAccount;
			
			if (evt.getStateChange() == ItemEvent.SELECTED) {
				Protocol protocol = (Protocol) evt.getItem();
				try {
					// Call the default constructor of that protocol specific
					// Account type. Properties will be set later.
					newAccount = protocol.getAccountClass().newInstance();
				} catch (InstantiationException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			} else {
				// If the item event is deselected, it means that no protocol
				// is specified. Create a StubAccount for this case.
				newAccount = new StubAccount();
			}

			newAccount.setName(oldAccount.getName());
			newAccount.setPassword(oldAccount.getPassword());
			newAccount.setConnectAutomatically(oldAccount.isConnectAutomatically());

			accountPanel = AccountPanelFactory.createAccountPanel(newAccount, this);
			accountPanelMap.put(newAccount, accountPanel);

			accountListModel.removeElementAt(index);
			accountListModel.addElement(newAccount, index);
			accountList.setSelectedIndex(index);
		}
	};

	/**
	 * This {@link Action} adds a new {@link Account} to the end of the
	 * {@link AccountListModel}.
	 */
	private Action addAccountAction = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Account account = new StubAccount();
			accountListModel.addElement(account);
			accountPanelMap.put(account, AccountPanelFactory.createAccountPanel(account, protocolListener));
			accountList.setSelectedIndex(accountListModel.getSize() - 1);
		}
	};

	/**
	 * This {@link Action} removes the selected {@link Account}s from the
	 * {@link AccountListModel}.
	 */
	private Action removeAccountAction = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] selectedIndices = accountList.getSelectedIndices();
			for (int i = selectedIndices.length - 1; i >= 0; i--) {
				accountPanelMap.remove(accountListModel.getElementAt(i));
				accountListModel.removeElementAt(i);
			}
		}
	};
	
	/**
	 * Creates a new {@link AccountManagerDialog}.
	 * 
	 * @param owner
	 *            The {@link Frame} from which this {@link JDialog} is displayed.
	 */
	public AccountManagerDialog(Frame owner, BurdeeRoot root) {
		super(owner, "Account Manager");
		this.root = root;
		buildUI();
	}
	
	/**
	 * Builds the dialog UI.
	 */
	private void buildUI() {
		setLayout(new MigLayout("fill", "", "[][grow 0]"));
		setMinimumSize(new Dimension(557, 583));
		
		buildSplitPane();
		buildAddRemoveButtons();
		buildOKCancelButtons();
		
		setIconImage(ACCOUNT_MANAGER_ICON.getImage());
	}

	/**
	 * Builds the {@link JSplitPane}, where the left component contains the
	 * account list, and the right component contains an account panel to modify
	 * a single account. The split pane is added to this dialog.
	 */
	private void buildSplitPane() {
		buildAccountList();
		buildAccountPanelMap();
		
		JScrollPane accountListScrollPane = new JScrollPane(accountList);
		accountListScrollPane.setMinimumSize(new Dimension(100, 500));
		
		splitPane = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT, 
				accountListScrollPane, 
				new JPanel(new MigLayout()));
		
		if (accountListModel.getSize() > 0) {
			accountList.setSelectedIndex(0);
		}
		splitPane.setDividerLocation(100);
		
		add(splitPane, "wmin 500, hmin 500, span, grow");
	}

	/**
	 * Builds the {@link JList} backed by the {@link AccountListModel}, and adds
	 * it to this dialog.
	 */
	private void buildAccountList() {
		final JPopupMenu popup = new JPopupMenu();
		
		final JMenuItem addAccountMenuItem = new JMenuItem(addAccountAction);
		addAccountMenuItem.setText("Add Account");
		addAccountMenuItem.setIcon(ADD_ICON);
		popup.add(addAccountMenuItem);
		
		final JMenuItem removeAccountMenuItem = new JMenuItem(removeAccountAction);
		removeAccountMenuItem.setText("Remove Account");
		removeAccountMenuItem.setIcon(REMOVE_ICON);
		removeAccountMenuItem.setVisible(false);
		popup.add(removeAccountMenuItem);
		
		/**
		 * This {@link ListSelectionListener} determines the last selected
		 * {@link Account} in the {@link JList} and displays its corresponding
		 * {@link AccountPanel} if it is not already shown.
		 */
		ListSelectionListener accountListListener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				JList list = ((JList) e.getSource());
				if (list.isSelectionEmpty()) {
					removeAccountMenuItem.setVisible(false);
					JPanel stubPanel = new JPanel(new MigLayout("width 500px, height 500px"));
					splitPane.setRightComponent(stubPanel);
				} else {
					int[] selected = list.getSelectedIndices();
					if (selected.length == 1) {
						removeAccountMenuItem.setText("Remove Account");
					} else {
						removeAccountMenuItem.setText("Remove Accounts");
					}
					removeAccountMenuItem.setVisible(true);
					
					Account account = ((AccountListModel) list.getModel()).getElementAt(selected[0]);
					accountPanel = accountPanelMap.get(account);
					if (splitPane.getRightComponent() != accountPanel) {
						splitPane.setRightComponent(accountPanel);
					}
				}
			}
		};
		
		accountListModel = new AccountListModel(root);
		accountList = new JList(accountListModel);
		accountList.setCellRenderer(new AccountListCellRenderer());
		accountList.addListSelectionListener(accountListListener);
		accountList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		accountList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				maybeShowPopup(e);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);
			}
			
			private void maybeShowPopup(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
	}

	/**
	 * Builds the {@link Map} of {@link Account}s to their corresponding
	 * {@link AccountPanel}s. The {@link AccountListModel} used inside this
	 * class must be initialized before this method gets called. The reason
	 * behind this is that the {@link AccountListModel} creates a copy of the
	 * {@link List} of child {@link Account} objects so that they are not
	 * directly modified until OK is pressed.
	 */
	private void buildAccountPanelMap() {
		for (Account account : accountListModel.getAccounts()) {
			accountPanelMap.put(account, AccountPanelFactory.createAccountPanel(account, protocolListener));
		}
	}

	/**
	 * Builds the add and remove {@link Account} buttons, and adds them to the
	 * bottom left of this dialog.
	 */
	private void buildAddRemoveButtons() {
		JButton addButton = new JButton(addAccountAction);
		JButton removeButton = new JButton(removeAccountAction);
		
		addButton.setIcon(ADD_ICON);
		removeButton.setIcon(REMOVE_ICON);
		
		add(addButton, "split 2");
		add(removeButton);
	}

	/**
	 * Builds the OK and Cancel buttons, and adds them to the bottom right of
	 * this dialog.
	 */
	private void buildOKCancelButtons() {
		JButton okButton = new JButton(new AbstractAction("OK") {
			@Override
			public void actionPerformed(ActionEvent e) {
				applyChanges();
				AccountManagerDialog.this.dispose();
			}
		});
		getRootPane().setDefaultButton(okButton);
		add(okButton, "tag ok, split 2");
		
		add(new JButton(new AbstractAction("Cancel") {
			@Override
			public void actionPerformed(ActionEvent e) {
				AccountManagerDialog.this.dispose();
			}
		}), "tag cancel");
	}

	/**
	 * Applies all of the changes made within this dialog. This includes added,
	 * removed, and changed {@link Account}s. 
	 */
	private void applyChanges() {
		accountListModel.applyChanges();
	}

}
