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

import java.util.EventObject;
import java.util.List;

/**
 * This {@link EventObject} represents either a child added or child removed
 * event on {@link BurdeeObject}, which should occur after a call to
 * {@link BurdeeObject#addChild(BurdeeObject)} or
 * {@link BurdeeObject#removeChild(BurdeeObject)}.
 */
public class BurdeeChildEvent extends EventObject {

	/**
	 * This enum defines child added and child removed events.
	 */
	public enum EventType {
		ADDED,
		REMOVED
	}
	
	/**
	 * @see #getChild()
	 */
	private final BurdeeObject child;
	
	/**
	 * @see #getIndex()
	 */
	private final int index;
	
	/**
	 * {@link #getType()}
	 */
	private final EventType type;

	/**
	 * Creates a new {@link BurdeeChildEvent}.
	 * 
	 * @param source
	 *            The parent {@link BurdeeObject} which the child has been added
	 *            to or removed from.
	 * @param child
	 *            The child {@link BurdeeObject} that has been added or removed.
	 * @param index
	 *            The index of the child in terms of the {@link List} of
	 *            children that belong to the parent {@link BurdeeObject},
	 *            defined by {@link BurdeeObject#getChildren()}.
	 * @param type
	 *            The event type; either a child added or child removed event.
	 */
	public BurdeeChildEvent(BurdeeObject source, BurdeeObject child, int index, EventType type) {
		super(source);
		this.child = child;
		this.index = index;
		this.type = type;
	}
	
	@Override
	public BurdeeObject getSource() {
		return (BurdeeObject) source;
	}

	/**
	 * Gets the parent {@link BurdeeObject}.
	 * 
	 * @return The parent {@link BurdeeObject}.
	 */
	public BurdeeObject getChild() {
		return child;
	}

	/**
	 * Gets the child {@link BurdeeObject}.
	 * 
	 * @return The child {@link BurdeeObject}.
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Gets the event type; either child added or removed.
	 * 
	 * @return The event type.
	 */
	public EventType getType() {
		return type;
	}

}
