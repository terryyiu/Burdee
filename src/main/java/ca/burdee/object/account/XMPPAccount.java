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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.SwingUtilities;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.util.StringUtils;

import ca.burdee.object.contact.Contact;
import ca.burdee.object.contact.Conversation;
import ca.burdee.object.contact.Status;
import ca.burdee.object.contact.XMPPConversation;

/**
 * This {@link Account} wrapper connects with an XMPP service. 
 */
public class XMPPAccount extends Account {
	
	/**
	 * @see #getDomain()
	 */
	private String domain;
	
	/**
	 * @see #getResource()
	 */
	private String resource;
	
	/**
	 * @see #getConnection()
	 */
	private XMPPConnection connection;
	
	/**
	 * @see #isAnonymous()
	 */
	private boolean anonymous;

	/**
	 * This {@link RosterListener} listens to changes to the XMPP roster, and
	 * translates those changes to {@link Contact} objects.
	 */
	private final RosterListener rosterListener = new RosterListener() {
		
		@Override
		public void presenceChanged(Presence presence) {
			String address = StringUtils.parseBareAddress(presence.getFrom());
			Contact contact = findContactByAddress(address);
			contact.setStatus(getStatusFromPresence(presence));
		}
		
		@Override
		public void entriesUpdated(Collection<String> addresses) {
			Roster roster = connection.getRoster();
			for (String address : addresses) {
				RosterEntry rosterEntry = roster.getEntry(address);
				Contact contact = findContactByAddress(address);
				contact.setName(rosterEntry.getName());
				contact.setAddress(rosterEntry.getUser());
			}
		}
		
		@Override
		public void entriesDeleted(Collection<String> addresses) {
			for (String address : addresses) {
				Contact contact = findContactByAddress(address);
				removeChild(contact);
			}
		}
		
		@Override
		public void entriesAdded(Collection<String> addresses) {
			Roster roster = connection.getRoster();
			for (String address : addresses) {
				addContact(roster.getEntry(address), 
						getStatusFromPresence(roster.getPresence(address)));
			}
		}
	};
	
	/**
	 * Creates a new {@link XMPPAccount} with no username or domain.
	 */
	public XMPPAccount() {
		super();
	}
	
	/**
	 * Creates a new {@link XMPPAccount} with a given username and domain.
	 * 
	 * @param username
	 *            The username for this account.
	 * @param domain
	 *            The domain name for this account.
	 */
	public XMPPAccount(String username, String domain) {
		super(username);
		setDomain(domain);
	}
	
	/**
	 * Gets the {@link XMPPConnection}.
	 * 
	 * @return The {@link XMPPConnection}.
	 */
	public XMPPConnection getConnection() {
		return connection;
	}

	@Override
	public synchronized void connect() throws FailedConnectionException {
		if (!isConnected()) {
			connection = new XMPPConnection(domain);
			try {
				connection.connect();
				if (isAnonymous()) {
					connection.loginAnonymously();
				} else if (resource == null) {
					connection.login(getName(), getPassword());
				} else {
					connection.login(getName(), getPassword(), resource);
				}
				
				firePropertyChanged("connection", null, connection);
				
			} catch (XMPPException e) {
				throw new FailedConnectionException("Could not connect and login to " +
						"XMPP account.", e);
			}
		}
	}
	
	@Override
	public void disconnect() {
		if (isConnected()) {
			connection.disconnect();
			connection = null;
			firePropertyChanged("connection", connection, null);
		}
	}

	@Override
	public boolean isConnected() {
		return connection != null && connection.isConnected();
	}

	/**
	 * Determines if this {@link XMPPAccount} is an anonymous account.
	 * 
	 * @return true if this is an anonymous account.
	 */
	public boolean isAnonymous() {
		return anonymous;
	}

	/**
	 * Sets the flag to determine if this {@link XMPPAccount} is an anonymous
	 * account.
	 * 
	 * @param anonymous
	 *            true if this is an anonymous account.
	 */
	public void setAnonymous(boolean anonymous) {
		boolean oldAnonymous = this.anonymous;
		this.anonymous = anonymous;
		firePropertyChanged("anonymous", oldAnonymous, anonymous);
	}

	/**
	 * Gets the XMPP domain name of this account.
	 * 
	 * @return The XMPP domain name.
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Sets the XMPP domain name of this account.
	 * 
	 * @param domain
	 *            The XMPP domain name.
	 */
	public void setDomain(String domain) {
		String oldServiceName = this.domain;
		this.domain = domain;
		firePropertyChanged("domain", oldServiceName, domain);
	}

	/**
	 * Gets the XMPP resource name of this account.
	 * 
	 * @return The XMPP resource name.
	 */
	public String getResource() {
		return resource;
	}

	/**
	 * Sets the XMPP resource name of this account.
	 * 
	 * @param resource
	 *            The XMPP resource name.
	 */
	public void setResource(String resource) {
		String oldResource = this.resource;
		this.resource = resource;
		firePropertyChanged("resource", oldResource, resource);
	}
	
	@Override
	public void updateToMatch(Account account) {
		if (!XMPPAccount.class.isAssignableFrom(account.getClass())) {
			throw new IllegalArgumentException("Cannot update an XMPP account " +
					"with account of type " + account.getClass() + ".");
		}
		XMPPAccount sourceAccount = (XMPPAccount) account;
		super.updateToMatch(sourceAccount);
		setDomain(sourceAccount.getDomain());
		setResource(sourceAccount.getResource());
		setAnonymous(sourceAccount.isAnonymous());
	}

	@Override
	public synchronized void populate() {
		if (isConnected()) {
			if (isPopulated()) {
				List<Contact> contacts = getChildren(Contact.class);
				for (int i = contacts.size() - 1; i >= 0; i--) {
					removeChild(contacts.get(i));
				}
				
				populated = false;
				firePropertyChanged("populated", true, false);
			}
			
			final Roster roster = connection.getRoster();
			
			for (RosterEntry entry : roster.getEntries()) {
				addContact(entry, getStatusFromPresence(roster.getPresence(entry.getUser())));
			}
			
			roster.addRosterListener(rosterListener);
			
			connection.getChatManager().addChatListener(new ChatManagerListener() {
				
				@Override
				public void chatCreated(final Chat chat, boolean createdLocally) {
					String address = StringUtils.parseBareAddress(chat.getParticipant());
					Conversation conversation = null;
					Contact contact = null;
					for (Conversation localConversation : getChildren(Conversation.class)) {
						for (Contact localContact : localConversation.getContacts()) {
							if (localContact.getAddress().equals(address)) {
								conversation = localConversation;
								contact = localContact;
								break;
							}
						}
					}

					if (conversation == null) {
						contact = findContactByAddress(address);
						if (contact == null) {
							throw new IllegalStateException("A chat was created but could not find its contact.");
						}
						conversation = createConversation(Collections.singletonList(contact));
					}

					final Conversation finalConversation = conversation;
					final Contact finalContact = contact;

					chat.addMessageListener(new MessageListener() {

						@Override
						public void processMessage(Chat chat, final Message message) {
							// Firing a message received event on the event
							// dispatching thread. This is hack to ensure that
							// the ConversationFrame and ConversationPanel for
							// this Conversation has been created before
							// sending the message to it.
							SwingUtilities.invokeLater(new Runnable() {
								
								@Override
								public void run() {
									finalConversation.fireMessageReceived(finalContact, message.getBody());
								}
							});
						}
					});
				}
			});
			
			populated = true;
			firePropertyChanged("populated", false, true);
		}
	}

	/**
	 * Adds a {@link Contact} to this {@link XMPPAccount}.
	 * 
	 * @param entry
	 *            The XMPP {@link RosterEntry} for this {@link Contact}.
	 * @param status
	 *            The {@link Status} of the {@link Contact}.
	 */
	private void addContact(RosterEntry entry, Status status) {
		Contact contact = new Contact(entry.getName(), entry.getUser(), status);
		addChild(contact);
	}

	/**
	 * Converts a {@link Presence} to a {@link Status} enumerated item.
	 * 
	 * @param presence
	 *            The {@link Presence} to convert.
	 * @return The {@link Status} equivalent of the {@link Presence}.
	 */
	public static Status getStatusFromPresence(Presence presence) {
		if (presence.isAvailable()) {
			Mode mode = presence.getMode();
			if (mode == Mode.away || mode == Mode.xa) {
				return Status.AWAY;
			} else if (mode == Mode.dnd) {
				return Status.BUSY;
			} else {
				return Status.ONLINE;
			}
		} else {
			return Status.OFFLINE;
		}
	}

	/**
	 * Finds the first {@link Contact} with a matching address.
	 * 
	 * @param address
	 *            The address to search for.
	 * @return The {@link Contact} with the matching address, or null if one
	 *         does not exist.
	 */
	private Contact findContactByAddress(String address) {
		for (Contact contact : getChildren(Contact.class)) {
			if (contact.getAddress().equals(address)) {
				return contact;
			}
		}
		return null;
	}

	@Override
	public XMPPConversation createConversation(List<Contact> contacts) {
		if (contacts == null || contacts.isEmpty()) {
			throw new IllegalArgumentException("Cannot create a conversation with no contacts.");
		}
		XMPPConversation conversation = new XMPPConversation(contacts.get(0));
		addChild(conversation);
		return conversation;
	}
	
}
