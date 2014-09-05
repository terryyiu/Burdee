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

package ca.burdee.object.contact;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import ca.burdee.object.BurdeeObject;
import ca.burdee.object.account.XMPPAccount;

/**
 * This {@link Conversation} is used for communicating over the XMPP protocol.
 * Currently, multi-user chat is not supported.
 */
public class XMPPConversation extends Conversation {

	/**
	 * Creates an {@link XMPPConversation} with a single {@link Contact}.
	 * 
	 * @param contact
	 *            The {@link Contact} to have the {@link Conversation} with.
	 */
	public XMPPConversation(Contact contact) {
		super(contact);
	}

	@Override
	public void sendMessage(String message) {
		XMPPConnection connection = getParent().getConnection();
		ChatManager chatManager = connection.getChatManager();
		
		// Multi-user chat is not supported yet.
		Contact contact = getContacts().get(0);
		
		Chat chat = chatManager.createChat(contact.getAddress(), null);
		try {
			chat.sendMessage(message);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fireMessageSent(message);
	}
	
	@Override
	public XMPPAccount getParent() {
		return (XMPPAccount) super.getParent();
	}
	
	@Override
	public void setParent(BurdeeObject parent) {
		if (parent != null && !(XMPPAccount.class.isAssignableFrom(parent.getClass()))) {
			throw new IllegalArgumentException(Conversation.class.getSimpleName() +
					" can only have parent of type " + XMPPAccount.class.getSimpleName() + ".");
		}
		super.setParent(parent);
	}

}
