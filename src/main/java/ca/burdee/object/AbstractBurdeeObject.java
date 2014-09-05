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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import ca.burdee.object.BurdeeChildEvent.EventType;
import ca.burdee.object.util.BurdeeUtils;

/**
 * An abstract implementation of {@link BurdeeObject}.
 */
public abstract class AbstractBurdeeObject implements BurdeeObject {
	
	/**
	 * The {@link List} of {@link BurdeeListener}s that listen on this
	 * {@link BurdeeObject}.
	 */
	private List<BurdeeListener> listeners = new ArrayList<BurdeeListener>();
	
	/**
	 * @see #getName()
	 */
	private String name;
	
	/**
	 * @see #getUuid()
	 */
	private final String uuid;
	
	/**
	 * @see #getParent()
	 */
	private BurdeeObject parent;
	
	/**
	 * Creates a new abstract implementation of {@link BurdeeObject}.
	 */
	public AbstractBurdeeObject() {
		uuid = UUID.randomUUID().toString();
	}
	
	@Override
	public void addChild(BurdeeObject child) {
		if (child == null) {
			throw new IllegalArgumentException("Cannot add null children to parent " + 
					getName() + " of type " + getClass() + ".");
		} else if (!allowsChildType(child.getClass())) {
			throw new IllegalArgumentException("Could not add child " + child.getName() +
					" of type " + child.getClass() + " because the parent " + getName() +
					" of type " + getClass() + " does not allow this child type");
		} else if (getChildren().contains(child)) {
			throw new IllegalArgumentException("Could not add child " + child.getName() +
					" of type " + child.getClass() + " because the parent " + getName() +
					" of type " + getClass() + " already contains this child.");
		}
		int index = getChildren(child.getClass()).size();
		addChildImpl(child, index);
		child.setParent(this);
		fireChildAdded(child, index);
	}

	@Override
	public void addChild(BurdeeObject child, int index) {
		if (child == null) {
			throw new IllegalArgumentException("Cannot add null children to parent " + 
					getName() + " of type " + getClass() + ".");
		} else if (!allowsChildType(child.getClass())) {
			throw new IllegalArgumentException("Could not add child " + child.getName() +
					" of type " + child.getClass() + " because the parent " + getName() +
					" of type " + getClass() + " does not allow this child type");
		} else if (getChildren().contains(child)) {
			throw new IllegalArgumentException("Could not add child " + child.getName() +
					" of type " + child.getClass() + " because the parent " + getName() +
					" of type " + getClass() + " already contains this child.");
		}
		
		addChildImpl(child, index);
		child.setParent(this);
		fireChildAdded(child, index);
	}

	/**
	 * The object specific add child implementation which is called by
	 * {@link #addChild(BurdeeObject)}. This method should never be called
	 * directly. {@link #addChild(BurdeeObject)} should be called instead as it
	 * performs an initial check to see if the child already exists in the
	 * parent's list of children and calls {@link #setParent(BurdeeObject)} on
	 * the child object.
	 * 
	 * @param child
	 *            The child {@link BurdeeObject} to add.
	 * @see #addChild(BurdeeObject)
	 */
	protected abstract void addChildImpl(BurdeeObject child, int index);

	@Override
	public boolean removeChild(BurdeeObject child) {
		if (!allowsChildren()) {
			throw new UnsupportedOperationException("Could not remove child " + 
					child.getName() + " of type " + child.getClass() + " because the parent " + 
					getName() + " of type " + getClass() + " does not allow children.");
		} else if (child == null) {
			throw new IllegalArgumentException("Cannot remove null children from parent " + 
					getName() + " of type " + getClass() + ".");
		} else if (!allowsChildType(child.getClass())) {
			throw new IllegalArgumentException("Could not remove child " + child.getName() +
					" of type " + child.getClass() + " because the parent " + getName() +
					" of type " + getClass() + " does not allow this child type");
		}
		int index = getChildren().indexOf(child);
		boolean removed = removeChildImpl(child);
		if (removed) {
			fireChildRemoved(child, index);
			setParent(null);
		}
		return removed;
	}

	/**
	 * The object specific remove child implementation which is called by
	 * {@link #removeChild(BurdeeObject)}. This method should never be called
	 * directly. {@link #removeChild(BurdeeObject)} should be called instead as
	 * it performs an initial null check and calls
	 * {@link #setParent(BurdeeObject)} on the child object to remove the parent
	 * reference.
	 * 
	 * @param child
	 *            The child {@link BurdeeObject} to remove.
	 * @return true if the child removal was successful.
	 * @see #removeChild(BurdeeObject)
	 */
	protected abstract boolean removeChildImpl(BurdeeObject child);
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		String oldName = this.name;
		this.name = name;
		firePropertyChanged("name", oldName, name);
	}
	
	@Override
	public String getUuid() {
		return uuid;
	}
	
	@Override
	public BurdeeObject getParent() {
		return parent;
	}
	
	@Override
	public void setParent(BurdeeObject parent) {
		BurdeeObject oldParent = this.parent;
		this.parent = parent;
		firePropertyChanged("parent", oldParent, parent);
	}
	
	@Override
	public boolean allowsChildType(Class<? extends BurdeeObject> type) {
		for (Class<? extends BurdeeObject> clazz : getAllowedChildTypes()) {
			if (clazz.isAssignableFrom(type)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean allowsChildren() {
		return !getAllowedChildTypes().isEmpty();
	}
	
	@Override
	public <T extends BurdeeObject> List<T> getChildren(Class<T> childType) {
		List<T> children = new ArrayList<T>();
		for (BurdeeObject child : getChildren()) {
			if (childType.isAssignableFrom(child.getClass())) {
				children.add(childType.cast(child));
			}
		}
		return Collections.unmodifiableList(children);
	}
	
	public void addBurdeeListener(BurdeeListener l) {
		synchronized(listeners) {
			// Ensures that a particular listener is not added twice.
			listeners.remove(l);

			listeners.add(l);
		}
	}
	
	public void removeBurdeeListener(BurdeeListener l) {
		synchronized(listeners) {
			listeners.remove(l);
		}
	}

	/**
	 * Fires a child added event to all of the {@link BurdeeListener}s that are
	 * attached to this {@link BurdeeObject}.
	 * 
	 * @param child
	 *            The child {@link BurdeeObject} that was added to this parent
	 *            {@link BurdeeObject}.
	 * @param index
	 *            The index of the added child in terms of the the {@link List}
	 *            returned by the {@link #getChildren(Class)} method for
	 *            siblings that are the same type as the added child.
	 */
	protected void fireChildAdded(BurdeeObject child, int index) {
		BurdeeChildEvent evt = new BurdeeChildEvent(this, child, index, EventType.ADDED);
		synchronized(listeners) {
			for (BurdeeListener listener : listeners) {
				listener.childAdded(evt);
			}
		}
	}

	/**
	 * Fires a child removed event to all of the {@link BurdeeListener}s that
	 * are attached to this {@link BurdeeObject}.
	 * 
	 * @param child
	 *            The child {@link BurdeeObject} that was removed from this
	 *            parent {@link BurdeeObject}.
	 * @param index
	 *            The index of the removed child in terms of the {@link List}
	 *            that would have been returned by the
	 *            {@link #getChildren(Class)} method, before the removal
	 *            occurrence, for siblings that are the same type as the removed
	 *            child.
	 */
	protected void fireChildRemoved(BurdeeObject child, int index) {
		BurdeeChildEvent evt = new BurdeeChildEvent(this, child, index, EventType.REMOVED);
		synchronized(listeners) {
			for (BurdeeListener listener : listeners) {
				listener.childRemoved(evt);
			}
		}
	}

	/**
	 * Fires a property change event to all of the {@link BurdeeListener}s that
	 * are attached to this {@link BurdeeObject}.
	 * 
	 * @param propertyName
	 *            The property name of the property that was changed.
	 * @param oldValue
	 *            The old value of the property. This value can be null.
	 * @param newValue
	 *            The new value of the property. This value can be null.
	 */
	protected void firePropertyChanged(String propertyName, Object oldValue, Object newValue) {
		if (!BurdeeUtils.isNullSafeEquals(oldValue, newValue)) {
			PropertyChangeEvent evt = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
			synchronized(listeners) {
				for (BurdeeListener listener : listeners) {
					listener.propertyChange(evt);
				}
			}
		}
	}
	
}
