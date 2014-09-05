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

import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import ca.burdee.object.account.Protocol;
import ca.burdee.object.account.StubAccount;

/**
 * This {@link AccountPanel} modifies a {@link StubAccount}, which contains only
 * basic properties that are common among any account type. The purpose of this
 * {@link StubAccountPanel} is to display the basic UI components for those
 * properties to be saved when the user chooses a protocol.
 */
public class StubAccountPanel extends AccountPanel<StubAccount> {

	/**
	 * Creates a new {@link StubAccountPanel}.
	 * 
	 * @param account
	 *            The {@link StubAccount} that this panel modifies.
	 * @param protocolListener
	 *            The {@link ItemListener} that listens to changes in selection
	 *            for the {@link Protocol} {@link JComboBox}.
	 */
	public StubAccountPanel(StubAccount account, ItemListener protocolListener) {
		super(account, protocolListener);
	}

}
