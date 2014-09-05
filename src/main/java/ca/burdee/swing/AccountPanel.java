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

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import net.miginfocom.swing.MigLayout;
import ca.burdee.object.account.Account;
import ca.burdee.object.account.Protocol;

/**
 * This {@link JPanel} is used for modifying a single {@link Account}. Note that
 * this panel does not create a copy of the {@link Account} it is modifying; it
 * is modified directly.
 * 
 * @param <T>
 *            The {@link Account} type that this panel modifies. UI components
 *            that delegate to specific properties of that {@link Account} type
 *            will be added to the panel.
 */
public abstract class AccountPanel<T extends Account> extends JPanel {
	
	/**
	 * The {@link Account} this panel is modifying.
	 */
	protected final T account;
	
	/**
	 * The {@link JComboBox} that allows choice of {@link Protocol}.
	 */
	private JComboBox protocolComboBox;

	/**
	 * The {@link ItemListener} that listens to selections in the
	 * {@link Protocol} {@link JComboBox}.
	 */
	private final ItemListener protocolListener;

	/**
	 * The {@link JTextField} that defines the username of the {@link Account}.
	 */
	private JTextField usernameTextField;

	/**
	 * The {@link JPasswordField} that defines the password of the
	 * {@link Account}.
	 */
	private JPasswordField passwordField;

	/**
	 * The {@link JCheckBox} that determines whether the {@link Account} should
	 * automatically connect on startup.
	 */
	private JCheckBox connectAutomaticallyCheckBox;

	/**
	 * Creates a new {@link AccountPanel}.
	 * 
	 * @param account
	 *            The {@link Account} to directly modify.
	 * @param protocolListener
	 *            The {@link ItemListener} to attach to the {@link Protocol}
	 *            choosing {@link JComboBox} in this panel.
	 */
	public AccountPanel(T account, ItemListener protocolListener) {
		super();
		this.account = account;
		this.protocolListener = protocolListener;
		
		buildUI();
	}
	
	/**
	 * Builds the panel UI.
	 */
	protected void buildUI() {
		setLayout(new MigLayout("width 500px, height 500px", "[grow,fill]", ""));
		
		buildProtocolComboBox();
		buildUsernameTextField();
		buildPasswordField();
		buildConnectAutomaticallyCheckBox();
	}

	/**
	 * Builds the {@link Protocol} chooser combo box and adds it to the panel.
	 */
	private void buildProtocolComboBox() {
		Protocol[] protocols = new Protocol[Protocol.values().length + 1];
		System.arraycopy(Protocol.values(), 0, protocols, 1, protocols.length - 1);
		Arrays.sort(protocols, new Comparator<Protocol>() {
			@Override
			public int compare(Protocol p1, Protocol p2) {
				if (p1 == p2) {
					return 0;
				} else if (p1 == null) {
					return -1;
				} else if (p2 == null) {
					return 1;
				} else {
					return p1.getName().compareToIgnoreCase(p2.getName());
				}
			}
		});
		
		protocolComboBox = new JComboBox(protocols);
		protocolComboBox.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
				if (value == null) {
					setText("<Please choose an IM protocol.>");
				} else if (value instanceof Protocol) {
					setText(((Protocol) value).getName());
				} else {
					throw new IllegalArgumentException(
							"Cannot render a value that is neither null or a Protocol.");
				}
				return this;
			}
		});
		
		Protocol protocol = Protocol.findByAccountClass(account.getClass());
		if (protocol != null) {
			protocolComboBox.setSelectedItem(protocol);
		}
		if (protocolListener != null) {
			protocolComboBox.addItemListener(protocolListener);
		}
		
		add(new JLabel("Protocol"), "span");
		add(protocolComboBox, "span");
	}

	/**
	 * Builds the {@link Account} username text field and adds it to the panel.
	 */
	private void buildUsernameTextField() {
		usernameTextField = new JTextField(account.getName());
		usernameTextField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateName(e.getDocument());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateName(e.getDocument());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				updateName(e.getDocument());
			}
			
			private void updateName(Document document) {
				try {
					account.setName(document.getText(0, document.getLength()));
				} catch (BadLocationException e) {
					throw new RuntimeException(e);
				}
			}
			
		});
		
		add(new JLabel("Username"), "span");
		add(usernameTextField, "span");
	}

	/**
	 * Builds the {@link Account} password field and adds it to the panel.
	 */
	private void buildPasswordField() {
		passwordField = new JPasswordField(account.getPassword());
		passwordField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				updatePassword(e.getDocument());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				updatePassword(e.getDocument());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				updatePassword(e.getDocument());
			}
			
			private void updatePassword(Document document) {
				try {
					account.setPassword(document.getText(0, document.getLength()));
				} catch (BadLocationException e) {
					throw new RuntimeException(e);
				}
			}
			
		});
		
		add(new JLabel("Password"), "span");
		add(passwordField, "span");
	}

	 private void buildConnectAutomaticallyCheckBox() {
		 connectAutomaticallyCheckBox = new JCheckBox("Connect Automatically");
		 connectAutomaticallyCheckBox.setSelected(account.isConnectAutomatically());
		 connectAutomaticallyCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				account.setConnectAutomatically(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		 
		 add(connectAutomaticallyCheckBox, "span");
	 }
	 
}
