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

/**
 * This {@link XMPPAccount} is specific to the Google Talk IM service, whose
 * domain is gmail.com.
 */
public class GoogleTalkAccount extends XMPPAccount {
	
	/**
	 * Creates a new {@link GoogleTalkAccount} with no username.
	 */
	public GoogleTalkAccount() {
		this(null);
	}

	/**
	 * Creates a new {@link GoogleTalkAccount} with a given username.
	 * 
	 * @param username The username of this Google Talk account.
	 */
	public GoogleTalkAccount(String username) {
		super(username, "gmail.com");
	}
	
}
