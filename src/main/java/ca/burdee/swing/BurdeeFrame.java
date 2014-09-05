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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

import ca.burdee.object.AbstractBurdeeListener;
import ca.burdee.object.BurdeeChildEvent;
import ca.burdee.object.BurdeeListener;
import ca.burdee.object.BurdeeRoot;
import ca.burdee.object.account.Account;
import ca.burdee.object.contact.Contact;
import ca.burdee.object.contact.Conversation;
import ca.burdee.swing.action.AboutAction;
import ca.burdee.swing.action.ExitAction;
import ca.burdee.swing.action.ManageAccountsAction;

/**
 * The main {@link JFrame} of the Burdee application.
 */
public class BurdeeFrame extends JFrame {

	/**
	 * The {@link JTree} containing the contact list.
	 */
	private JTree contactList;

	/**
	 * The {@link BurdeeRoot} object to use as the root of the contact list
	 * tree.
	 */
	private final BurdeeRoot root;
	
	/**
	 * The File {@link JMenu}.
	 */
	private JMenu fileMenu;

	/**
	 * The {@link ConversationFrame} where all {@link Conversation}s take place.
	 */
	private ConversationFrame conversationFrame;

	/**
	 * This {@link ImageIcon} is used to render the icon for the About
	 * {@link JMenuItem}.
	 */
	public static final ImageIcon ABOUT_ICON = 
		new ImageIcon(BurdeeFrame.class.getResource("/icons/information.png"));

	/**
	 * This {@link ImageIcon} is used to render the icon for the Manage Accounts
	 * {@link JMenuItem}.
	 */
	public static final ImageIcon MANAGE_ACCOUNTS_ICON = 
		new ImageIcon(BurdeeFrame.class.getResource("/icons/group.png"));
	
	/**
	 * Check that we are on Mac OS X.
	 */
	public static boolean MAC_OS_X = (System.getProperty("os.name").toLowerCase().startsWith("mac os x"));
	
	/**
	 * This {@link BurdeeListener} listens to newly created {@link Conversation}s.
	 */
	private final BurdeeListener conversationListener = new AbstractBurdeeListener() {
		
		@Override
		public void childAdded(final BurdeeChildEvent evt) {
			if (Account.class.isAssignableFrom(evt.getChild().getClass())) {
				evt.getChild().addBurdeeListener(this);
			} else if (conversationFrame == null && 
					Account.class.isAssignableFrom(evt.getSource().getClass()) &&
					evt.getChild() instanceof Conversation) {
				
				// We want to create the ConversationFrame on the event
				// dispatching thread or else the application will be in an
				// inconsistent state with listeners.
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						showConversation((Conversation) evt.getChild());
					}
				});
			}
		}
		
		public void childRemoved(BurdeeChildEvent evt) {
			if (Account.class.isAssignableFrom(evt.getChild().getClass())) {
				evt.getChild().removeBurdeeListener(this);
			}
		}
	};

	/**
	 * Creates a new {@link BurdeeFrame}.
	 * 
	 * @param root
	 *            The {@link BurdeeRoot} that is the root of the contact
	 *            list tree.
	 */
	public BurdeeFrame(final BurdeeRoot root) {
		super("Burdee");
		this.root = root;
		
		root.addBurdeeListener(conversationListener);
		for (Account account : root.getChildren(Account.class)) {
			account.addBurdeeListener(conversationListener);
		}
		
		buildUI();
	}

	/**
	 * Builds the {@link BurdeeFrame} UI.
	 */
	private void buildUI() {
		buildMenuBar();
		buildContactList();
		setPreferredSize(new Dimension(250, 500));
	}
	
	/**
	 * Builds the frame's menu bar.
	 */
	private void buildMenuBar() {
		setJMenuBar(new JMenuBar());
		buildFileMenu();
		buildHelpMenu();
	}
	
	/**
	 * Builds the File menu in the menu bar.
	 */
	private void buildFileMenu() {
		fileMenu = new JMenu("File");
		buildAccountsMenu();
		
		if (!MAC_OS_X) {
			fileMenu.addSeparator();
			buildExitMenuItem();
		}
		
		getJMenuBar().add(fileMenu);
	}
	
	/**
	 * Builds the Accounts menu in the File menu.
	 */
	private void buildAccountsMenu() {
		JMenu accountsMenu = new JMenu("Accounts");
		JMenuItem manageAccountsMenuItem = 
			new JMenuItem(new ManageAccountsAction(this, root));
		manageAccountsMenuItem.setIcon(MANAGE_ACCOUNTS_ICON);
		accountsMenu.add(manageAccountsMenuItem);
		fileMenu.add(accountsMenu);
	}
	
	/**
	 * Builds the Exit menu item in the File menu.
	 */
	private void buildExitMenuItem() {
		JMenuItem exitMenuItem = new JMenuItem(new ExitAction());
		fileMenu.add(exitMenuItem);
	}
	
	/**
	 * Builds the Help menu in the menu bar.
	 */
	private void buildHelpMenu() {
		if (!MAC_OS_X) {
			JMenu helpMenu = new JMenu("Help");
			JMenuItem aboutMenuItem = new JMenuItem(new AboutAction(this));
			aboutMenuItem.setIcon(ABOUT_ICON);
			helpMenu.add(aboutMenuItem);
			getJMenuBar().add(helpMenu);
		}
	}
	
	/**
	 * Builds the contact list tree.
	 */
	private void buildContactList() {
		contactList = new JTree(new ContactListTreeModel(root));
		contactList.setCellRenderer(new ContactListTreeCellRenderer());
		contactList.setRootVisible(false);
		contactList.setShowsRootHandles(true);
		contactList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && !contactList.isSelectionEmpty()) {
					Object selectedComponent = 
						contactList.getSelectionPath().getLastPathComponent();
					if (selectedComponent instanceof Contact) {
						Contact contact = (Contact) selectedComponent;
						Conversation conversation = null;
						
						for (Conversation c : contact.getParent().getChildren(Conversation.class)) {
							if (c.getContacts().size() == 1 && c.getContacts().contains(contact)) {
								conversation = c;
								break;
							}
						}
						
						if (conversation == null) {
							conversation = contact.getParent().createConversation(
									Collections.singletonList(contact));
						}
						
						showConversation(conversation);
					}
				}
			}
		});
		
		add(new JScrollPane(contactList));
	}

	/**
	 * Displays the {@link ConversationFrame}, selecting the tab for the given
	 * {@link Conversation}. If the {@link ConversationFrame} does not exist
	 * yet, one is created. Otherwise, focus is simply given back to the
	 * {@link ConversationFrame}.
	 * 
	 * @param conversation
	 *            The {@link Conversation} to display in the
	 *            {@link ConversationFrame}.
	 */
	private void showConversation(Conversation conversation) {
		if (conversationFrame == null) {
			conversationFrame = new ConversationFrame(root);
			conversationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			conversationFrame.pack();
			
			conversationFrame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					conversationFrame = null;
				}
			});
		}
		conversationFrame.setSelectedTab(
				conversation.getParent().getChildren(Conversation.class).size() - 1);
		conversationFrame.setVisible(true);
	}
	
}
