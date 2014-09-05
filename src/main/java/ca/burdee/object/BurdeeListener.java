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
import java.beans.PropertyChangeListener;

/**
 * This {@link PropertyChangeListener} listens to child added, child removed,
 * and property change events on a {@link BurdeeObject}.
 */
public interface BurdeeListener extends PropertyChangeListener {

	/**
	 * This event is fired every time a {@link BurdeeObject} is added as a child
	 * of another {@link BurdeeObject}.
	 * 
	 * @param evt
	 *            The {@link BurdeeChildEvent} that contains information about
	 *            the event.
	 */
	void childAdded(BurdeeChildEvent evt);

	/**
	 * This event is fired every time a child {@link BurdeeObject} is removed
	 * from its parent {@link BurdeeObject}.
	 * 
	 * @param evt
	 *            The {@link BurdeeChildEvent} that contains information about
	 *            the event.
	 */
	void childRemoved(BurdeeChildEvent evt);

	/**
	 * This event is fired every time a property on a {@link BurdeeObject} is
	 * changed.
	 * 
	 * @param evt
	 *            The {@link PropertyChangeEvent} that contains information
	 *            about this event.
	 */
	void propertyChange(PropertyChangeEvent evt);

}
