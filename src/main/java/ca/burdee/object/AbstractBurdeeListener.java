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

package ca.burdee.object;

import java.beans.PropertyChangeEvent;

/**
 * This is a stub {@link BurdeeListener}. If a listener that does not need to
 * listen to all 3 events (child added, child removed, property changed) can
 * extend from this class and implement the required methods.
 */
public class AbstractBurdeeListener implements BurdeeListener {

	@Override
	public void childAdded(BurdeeChildEvent evt) {
		// Stub method.
		// Extending classes should implement this method as needed.
	}

	@Override
	public void childRemoved(BurdeeChildEvent evt) {
		// Stub method.
		// Extending classes should implement this method as needed.
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// Stub method.
		// Extending classes should implement this method as needed.
	}

}
