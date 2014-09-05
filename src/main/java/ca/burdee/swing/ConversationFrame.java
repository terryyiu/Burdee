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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import ca.burdee.object.AbstractBurdeeListener;
import ca.burdee.object.BurdeeChildEvent;
import ca.burdee.object.BurdeeListener;
import ca.burdee.object.BurdeeRoot;
import ca.burdee.object.account.Account;
import ca.burdee.object.contact.Contact;
import ca.burdee.object.contact.Conversation;

/**
 * This {@link JFrame} displays all of the {@link Conversation}s the user is
 * having with different {@link Contact}s. A {@link JTabbedPane} is embedded
 * inside, where each tab represents a single {@link Conversation}.
 */
public class ConversationFrame extends JFrame {

	/**
	 * The {@link BurdeeRoot} that contain the {@link Account} objects which
	 * have the {@link Conversation}s as children.
	 */
	private final BurdeeRoot root;

	/**
	 * The {@link JTabbedPane} that contains s {@link Conversation} in each of
	 * its tabs.
	 */
	private JTabbedPane tabbedPane;

	/**
	 * This {@link BurdeeListener} listens to newly created and closed
	 * {@link Conversation}s.
	 */
	private BurdeeListener conversationListener = new AbstractBurdeeListener() {
		@Override
		public void childAdded(BurdeeChildEvent evt) {
			if (evt.getChild() instanceof Conversation) {
				evt.getChild().addBurdeeListener(this);
				tabbedPane.insertTab(
						evt.getChild().getName(), 
						null, 
						new ConversationPanel((Conversation) evt.getChild()), 
						null, 
						evt.getIndex());
			}
		}
		
		public void childRemoved(BurdeeChildEvent evt) {
			if (evt.getChild() instanceof Conversation) {
				evt.getChild().removeBurdeeListener(this);
				
				for (int i = 0; i < tabbedPane.getTabCount(); i++) {
					if (((ConversationPanel) tabbedPane.getTabComponentAt(i)).getConversation() == evt.getChild()) {
						tabbedPane.removeTabAt(i);
						break;
					}
				}
				
			}
		}
	};

	/**
	 * Creates a new {@link ConversationFrame}. Newly added and closed
	 * {@link Conversation}s are automatically displayed as such in its
	 * {@link JTabbedPane}. When this {@link JFrame} closes, all of the
	 * {@link Conversation}s are removed from their {@link Account}s.
	 */
	public ConversationFrame(final BurdeeRoot root) {
		super();
		this.root = root;
		
		for (Account account : root.getChildren(Account.class)) {
			account.addBurdeeListener(conversationListener);
			for (Conversation conversation : account.getChildren(Conversation.class)) {
				conversation.addBurdeeListener(conversationListener);
			}
		}
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				for (Account account : root.getChildren(Account.class)) {
					account.removeBurdeeListener(conversationListener);
					List<Conversation> conversations = account.getChildren(Conversation.class);
					for (int i = conversations.size() - 1; i >= 0; i--) {
						Conversation conversation = conversations.get(i);
						conversation.removeBurdeeListener(conversationListener);
						account.removeChild(conversation);
					}
				}
			}
		});
		
		buildUI();
	}
	
	/**
	 * Builds the UI.
	 */
	private void buildUI() {
		setLayout(new MigLayout("fill"));
		buildTabbedPane();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * Builds the tabbed pane. Each tab represents a single conversation. The
	 * title of the {@link ConversationFrame} is the title of the selected tab.
	 */
	private void buildTabbedPane() {
		tabbedPane = new JTabbedPane();
		for (Account account : root.getChildren(Account.class)) {
			for (Conversation conversation : account.getChildren(Conversation.class)) {
				tabbedPane.addTab(conversation.getName(), new ConversationPanel(conversation));
			}
		}
		
		tabbedPane.getModel().addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				setTitle(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
			}
		});
		
		tabbedPane.setSelectedIndex(0);
		
		add(tabbedPane, "span, grow");
	}

	/**
	 * Sets the selected tab to the given index. This method simply delegates
	 * the call to the {@link JTabbedPane}.
	 * 
	 * @param index
	 *            The index to be selected.
	 */
	public void setSelectedTab(int index) {
		tabbedPane.setSelectedIndex(index);
	}

}
