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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;
import ca.burdee.object.contact.Contact;
import ca.burdee.object.contact.Conversation;
import ca.burdee.object.contact.ConversationListener;

/**
 * This {@link JPanel} displays the messages sent and received to a single
 * {@link Conversation}.
 */
public class ConversationPanel extends JPanel {
	
	/**
	 * The {@link Conversation} that this panel displays.
	 */
	private final Conversation conversation;

	/**
	 * The {@link JScrollPane} that embeds the {@link JTextPane} that displays
	 * the ongoing {@link Conversation} between the user and the {@link Contact}s.
	 */
	private JScrollPane conversationScrollPane;

	/**
	 * The {@link JTextPane} that displays the ongoing {@link Conversation}
	 * between the user and the {@link Contact}s.
	 */
	private JTextPane conversationTextPane;

	/**
	 * The {@link JScrollPane} that embeds the {@link JTextPane} that displays
	 * the text that the user is typing in that will eventually be shown in the
	 * {@link #conversationTextPane} once the message is sent.
	 */
	private JScrollPane outgoingMessageScrollPane;

	/**
	 * The {@link JTextPane} that displays the text that the user is typing in
	 * that will eventually be shown in the {@link #conversationTextPane} once
	 * the message is sent.
	 */
	private JTextPane outgoingMessageTextPane;

	/**
	 * Creates a new {@link ConversationPanel}.
	 * 
	 * @param conversation
	 *            The {@link Conversation} that this panel displays.
	 */
	public ConversationPanel(Conversation conversation) {
		super(new MigLayout("fill"));
		this.conversation = conversation;
		
		conversation.addConversationListener(new ConversationListener() {
			
			@Override
			public void messageSent(Conversation conversation, String message) {
				conversationTextPane.setText(
						String.format(
								"%s\nme:\t%s", 
								conversationTextPane.getText(), 
								message));
			}
			
			@Override
			public void messageReceived(Conversation conversation, Contact contact,
					String message) {
				conversationTextPane.setText(
						String.format(
								"%s\n%s:\t%s", 
								conversationTextPane.getText(), 
								contact.getAddress(), message));
			}
		});
		
		buildUI();
	}
	
	/**
	 * Builds the UI.
	 */
	private void buildUI() {
		buildSplitPane();
	}

	/**
	 * Builds the {@link JSplitPane}, which contains the messages that have
	 * already been sent and received on the top, and the messages that the user
	 * is typing but has not sent on the bottom.
	 */
	private void buildSplitPane() {
		buildConversationPane();
		buildOutgoingMessagePane();
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, conversationScrollPane, outgoingMessageScrollPane);
		add(splitPane, "span, grow");
	}

	/**
	 * Builds the conversation pane, which contains the messages that have
	 * already been sent and received.
	 */
	private void buildConversationPane() {
		conversationTextPane = new JTextPane();
		conversationTextPane.setEditable(false);
		conversationScrollPane = new JScrollPane(conversationTextPane);
	}

	/**
	 * Builds the outgoing message pane, which contains the messages that the
	 * user is typing but has not sent.
	 */
	private void buildOutgoingMessagePane() {
		outgoingMessageTextPane = new JTextPane();
		
		outgoingMessageTextPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && 
						!e.isControlDown() &&
						!e.isShiftDown()) {
					final String outgoingMessage = outgoingMessageTextPane.getText();
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							if (outgoingMessage != null && !outgoingMessage.equals("")) {
								conversation.sendMessage(outgoingMessage);
							}
							outgoingMessageTextPane.setText("");
						}
					});
				}
			}
		});
		
		outgoingMessageScrollPane = new JScrollPane(outgoingMessageTextPane);
	}

	/**
	 * Gets the {@link Conversation} that this panel displays.
	 * 
	 * @return The {@link Conversation} tied to this panel.
	 */
	public Conversation getConversation() {
		return conversation;
	}

}
