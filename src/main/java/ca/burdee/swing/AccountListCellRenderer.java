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

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import ca.burdee.object.account.Account;

/**
 * This {@link ListCellRenderer} renders a {@link JList} that is backed by an
 * {@link AccountListModel}. The elements of this list are always of type
 * {@link Account}.
 */
public class AccountListCellRenderer extends DefaultListCellRenderer {

	/**
	 * This {@link ImageIcon} is used to render an {@link Account} list element.
	 */
	public static final ImageIcon ACCOUNT_ICON = 
		new ImageIcon(AccountListCellRenderer.class.getResource("/icons/user.png"));
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected,
				cellHasFocus);
		if (value != null && Account.class.isAssignableFrom(value.getClass())) {
			setText(((Account) value).getName());
			setIcon(ACCOUNT_ICON);
		} else {
			throw new IllegalArgumentException("Cannot render a value that is not an account.");
		}
		return this;
	}

}
