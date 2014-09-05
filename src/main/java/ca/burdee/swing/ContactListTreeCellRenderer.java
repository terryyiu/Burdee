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

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import ca.burdee.object.BurdeeObject;
import ca.burdee.object.BurdeeRoot;
import ca.burdee.object.account.Account;
import ca.burdee.object.contact.Contact;
import ca.burdee.object.contact.ContactCategory;
import ca.burdee.object.contact.Status;

/**
 * This {@link TreeCellRenderer} renders nodes from the tree represented by the
 * {@link ContactListTreeModel}.
 */
public class ContactListTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * This {@link ImageIcon} is used to render an {@link Account} tree node.
	 */
	public static final ImageIcon ACCOUNT_ICON = 
		new ImageIcon(AccountListCellRenderer.class.getResource("/icons/user.png"));

	/**
	 * This {@link ImageIcon} is used to render a {@link ContactCategory} tree
	 * node.
	 */
	public static final ImageIcon CONTACT_CATEGORY_ICON = 
		new ImageIcon(AccountListCellRenderer.class.getResource("/icons/group.png"));
	
	/**
	 * This {@link ImageIcon} is used to render a {@link Contact} tree node
	 * whose {@link Status} is {@link Status#ONLINE}.
	 */
	public static final ImageIcon CONTACT_ONLINE_ICON = 
		new ImageIcon(ContactListTreeCellRenderer.class.getResource("/icons/status_online.png"));

	/**
	 * This {@link ImageIcon} is used to render a {@link Contact} tree node
	 * whose {@link Status} is {@link Status#BUSY}.
	 */
	public static final ImageIcon CONTACT_BUSY_ICON = 
		new ImageIcon(ContactListTreeCellRenderer.class.getResource("/icons/status_busy.png"));

	/**
	 * This {@link ImageIcon} is used to render a {@link Contact} tree node
	 * whose {@link Status} is {@link Status#AWAY}.
	 */
	public static final ImageIcon CONTACT_AWAY_ICON = 
		new ImageIcon(ContactListTreeCellRenderer.class.getResource("/icons/status_away.png"));

	/**
	 * This {@link ImageIcon} is used to render a {@link Contact} tree node
	 * whose {@link Status} is {@link Status#OFFLINE}.
	 */
	public static final ImageIcon CONTACT_OFFLINE_ICON = 
		new ImageIcon(ContactListTreeCellRenderer.class.getResource("/icons/status_offline.png"));
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		
		if (value instanceof BurdeeRoot || 
				Account.class.isAssignableFrom(value.getClass()) || 
				value instanceof ContactCategory || 
				value instanceof Contact) {
			setText(((BurdeeObject) value).getName());
		} else {
			throw new IllegalArgumentException("Could not get the contact list " +
					"tree cell renderer component for node " + value.toString() + 
					" because it is not a " + BurdeeRoot.class + ", " + 
					Account.class + ", " + ContactCategory.class + " or " + 
					Contact.class + ".");
		}
		
		if (Account.class.isAssignableFrom(value.getClass())) {
			setIcon(ACCOUNT_ICON);
		} else if (value instanceof ContactCategory) {
			setIcon(CONTACT_CATEGORY_ICON);
		} else if (value instanceof Contact) {
			Contact contact = (Contact) value;
			if (contact.getName() == null) {
				setText(contact.getAddress());
			} else {
				setText(contact.getName());
			}
			setToolTipText(contact.getAddress());
			
			if (contact.getStatus() == Status.ONLINE) {
				setIcon(CONTACT_ONLINE_ICON);
			} else if (contact.getStatus() == Status.BUSY) {
				setIcon(CONTACT_BUSY_ICON);
			} else if (contact.getStatus() == Status.AWAY) {
				setIcon(CONTACT_AWAY_ICON);
			} else if (contact.getStatus() == Status.OFFLINE) {
				setIcon(CONTACT_OFFLINE_ICON);
			}
		}
		
		return this;
	}

}
