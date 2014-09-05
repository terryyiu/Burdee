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
import java.util.List;
import java.util.UUID;

/**
 * An arbitrary object within the Burdee chat client that can have properties
 * and children.
 */
public interface BurdeeObject {

	/**
	 * Gets the name of this {@link BurdeeObject}.
	 * 
	 * @return The name of this {@link BurdeeObject}.
	 */
	String getName();

	/**
	 * Sets the name of this {@link BurdeeObject}.
	 * 
	 * @param name
	 *            The name to set.
	 */
	void setName(String name);

	/**
	 * Gets the universally unique identifier for this {@link BurdeeObject}.
	 * 
	 * @return The {@link String} representation of the {@link UUID} of this
	 *         {@link BurdeeObject}.
	 */
	String getUuid();

	/**
	 * Gets the {@link List} of child {@link BurdeeObject}s that belong to this
	 * parent {@link BurdeeObject}. The ordering of this {@link List} is defined
	 * by {@link #getAllowedChildTypes()}. Whenever the ordering of this
	 * {@link List} is changed, ensure that {@link #getAllowedChildTypes()} is
	 * consistent with this change.
	 * 
	 * @return The {@link List} of child {@link BurdeeObject}s.
	 */
	List<? extends BurdeeObject> getChildren();

	/**
	 * Gets the {@link List} of child {@link BurdeeObject}s of type T that
	 * belong to this parent {@link BurdeeObject}.
	 * 
	 * @param <T>
	 *            The child class type to return the {@link List} of children
	 *            for.
	 * @param childType
	 *            The child class.
	 * @return The {@link List} of child {@link BurdeeObject}s of type T.
	 */
	<T extends BurdeeObject> List<T> getChildren(Class<T> childType);

	/**
	 * Adds a child {@link BurdeeObject} to this parent {@link BurdeeObject} at
	 * the end of the {@link List} of children of the same type.
	 * 
	 * @param child
	 *            The child {@link BurdeeObject} to add.
	 */
	void addChild(BurdeeObject child);

	/**
	 * Adds a child {@link BurdeeObject} to this parent {@link BurdeeObject}.
	 * 
	 * @param child
	 *            The child {@link BurdeeObject} to add.
	 * @param index
	 *            The index of the child relative to the {@link List} of childen
	 *            of the same type.
	 */
	void addChild(BurdeeObject child, int index);

	/**
	 * Removes a child {@link BurdeeObject} from this parent
	 * {@link BurdeeObject}
	 * 
	 * @param child
	 *            The child {@link BurdeeObject} to remove.
	 * @return true if the child removal was successful.
	 */
	boolean removeChild(BurdeeObject child);

	/**
	 * Checks if this {@link BurdeeObject} allows child {@link BurdeeObject}s to
	 * be added.
	 * 
	 * @return true if this {@link BurdeeObject} allows having child
	 *         {@link BurdeeObject}.
	 */
	boolean allowsChildren();

	/**
	 * Gets the {@link List} of allowed child {@link BurdeeObject} types this
	 * parent {@link BurdeeObject} allows. The order of this {@link List}
	 * defines the ordering of {@link #getChildren()}.
	 * 
	 * @return The {@link List} of allowed child {@link BurdeeObject} types.
	 */
	List<Class<? extends BurdeeObject>> getAllowedChildTypes();

	/**
	 * Determine if a given {@link BurdeeObject} {@link Class} is a valid child
	 * type of this parent {@link BurdeeObject}.
	 * 
	 * @param type
	 *            The {@link Class} of the child {@link BurdeeObject}.
	 * @return true if the given {@link BurdeeObject} {@link Class} type is a
	 *         valid child type.
	 */
	boolean allowsChildType(Class<? extends BurdeeObject> type);

	/**
	 * Gets the parent {@link BurdeeObject} that contains this child.
	 * 
	 * @return The parent {@link BurdeeObject} that contains this child.
	 */
	BurdeeObject getParent();

	/**
	 * Sets the parent {@link BurdeeObject} that this child belongs to.
	 * 
	 * @param parent
	 *            The parent {@link BurdeeObject}.
	 */
	void setParent(BurdeeObject parent);

	/**
	 * Adds a {@link BurdeeListener} to this {@link BurdeeObject}. Whenever a
	 * child {@link BurdeeObject} is added or removed from this
	 * {@link BurdeeObject} parent, this listener will get notified of a
	 * {@link BurdeeChildEvent}. Also, whenever a property of this
	 * {@link BurdeeObject} is changed, this listener will be notified of a
	 * {@link PropertyChangeEvent}.
	 * 
	 * @param l
	 *            The {@link BurdeeListener} to attach to this
	 *            {@link BurdeeObject}.
	 */
	void addBurdeeListener(BurdeeListener l);

	/**
	 * Removes a {@link BurdeeListener} from this {@link BurdeeObject}. Whenever
	 * a child added, child removed, or property change occurs on this
	 * {@link BurdeeObject}, this listener will no longer be notified of such an
	 * event. If this listener is not attached to this {@link BurdeeObject},
	 * calling this method will have no effect.
	 * 
	 * @param l
	 *            The {@link BurdeeListener} to detach from this
	 *            {@link BurdeeObject}.
	 */
	void removeBurdeeListener(BurdeeListener l);
	
}
