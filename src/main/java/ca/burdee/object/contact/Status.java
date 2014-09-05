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

/**
 * This enum represents a {@link Contact}'s online status.
 * 
 * XXX The assumption made here is that a {@link Contact} can only be either
 * online, busy, away, or offline. However, this may change upon further
 * research into the XMPP protocol and Smack API.
 */
public enum Status {
	ONLINE,
	BUSY,
	AWAY,
	OFFLINE
}
