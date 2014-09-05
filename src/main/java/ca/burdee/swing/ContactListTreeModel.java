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

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import ca.burdee.object.AbstractBurdeeListener;
import ca.burdee.object.BurdeeChildEvent;
import ca.burdee.object.BurdeeListener;
import ca.burdee.object.BurdeeObject;
import ca.burdee.object.BurdeeObjectNameComparator;
import ca.burdee.object.BurdeeRoot;
import ca.burdee.object.account.Account;
import ca.burdee.object.contact.Contact;
import ca.burdee.object.contact.ContactCategory;
import ca.burdee.object.contact.Conversation;
import ca.burdee.object.util.BurdeeUtils;

/**
 * This {@link TreeModel} defines how a contact list tree is laid out. Note that
 * the {@link Contact} nodes do not have to be unique in the tree. The hierarchy
 * is as follows:
 * 
 * {@link BurdeeRoot}
 *    -> {@link Account}
 *       -> {@link Contact}
 *       -> {@link ContactCategory}
 *          -> {@link Contact}
 */
public class ContactListTreeModel implements TreeModel {

	/**
	 * The {@link BurdeeRoot} object that is the top object of this
	 * {@link TreeModel}.
	 */
	private final BurdeeRoot root;

	/**
	 * The {@link List} of {@link TreeModelListener} that listen to events on
	 * this {@link TreeModel}.
	 */
	private final List<TreeModelListener> treeModelListeners = new ArrayList<TreeModelListener>();

	/**
	 * This {@link BurdeeListener} listens to added and removed {@link Account}
	 * s, {@link Contact}s, and {@link ContactCategory}s. It also listens to any
	 * property changes on any of these objects. On these events, the tree node
	 * for that object is refreshed.
	 */
	private final BurdeeListener accountAdditionListener = new AbstractBurdeeListener() {
		@Override
		public void childAdded(BurdeeChildEvent evt) {
			if (evt.getSource() instanceof Conversation || 
					evt.getChild() instanceof Conversation) {
				return;
			}
			
			BurdeeUtils.listenToHierarchy(evt.getChild(), this);
			List<BurdeeObject> ancestorList = new ArrayList<BurdeeObject>(BurdeeUtils.getAncestorList(evt.getChild()));
			ancestorList.remove(evt.getChild());
			Object[] ancestorArray = ancestorList.toArray();
			TreeModelEvent event = new TreeModelEvent(this, ancestorArray);
			
			for (TreeModelListener l : treeModelListeners) {
				l.treeStructureChanged(event);
			}
		}
		@Override
		public void childRemoved(BurdeeChildEvent evt) {
			BurdeeUtils.unlistenFromHierarchy(evt.getChild(), this);
			
			if (evt.getSource() instanceof Conversation || 
					evt.getChild() instanceof Conversation) {
				return;
			}
			
			List<BurdeeObject> ancestorList = new ArrayList<BurdeeObject>(BurdeeUtils.getAncestorList(evt.getChild()));
			ancestorList.remove(evt.getChild());
			Object[] ancestorArray = ancestorList.toArray();
			TreeModelEvent event = new TreeModelEvent(this, ancestorArray);
			
			for (TreeModelListener l : treeModelListeners) {
				l.treeStructureChanged(event);
			}
		}
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getSource() instanceof Conversation) {
				return;
			}
			
			Object[] ancestorArray = BurdeeUtils.getAncestorList(
					(BurdeeObject) evt.getSource()).toArray();
			TreeModelEvent event = new TreeModelEvent(this, ancestorArray);
			
			for (TreeModelListener l : treeModelListeners) {
				l.treeNodesChanged(event);
			}
		}
	};
	
	/**
	 * Creates a new {@link ContactListTreeModel}.
	 * 
	 * @param root
	 *            The {@link BurdeeRoot} object that is the top object of this
	 *            {@link TreeModel}.
	 */
	public ContactListTreeModel(final BurdeeRoot root) {
		this.root = root;
		
		BurdeeUtils.listenToHierarchy(root, accountAdditionListener);
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		treeModelListeners.add(l);
	}

	@Override
	public Object getChild(Object parent, int index) {
		if (parent instanceof Conversation) {
			return null;
		}
		
		List<? extends BurdeeObject> children;
		if (parent instanceof ContactCategory) {
			children = ((ContactCategory) parent).getContacts();
		} else {
			children = ((BurdeeObject) parent).getChildren(); 
		}
		
		if (index < 0 || index >= children.size()) {
			return null;
		}
		
		if (Account.class.isAssignableFrom(parent.getClass()) &&
				index >= (children.size() - 
						((Account) parent).getChildren(Conversation.class).size())) {
			return null;
		}
		
		children = new ArrayList<BurdeeObject>(children);
		
		if (Account.class.isAssignableFrom(parent.getClass())) {
			for (int i = children.size() - 1; i >= 0; i--) {
				if (children.get(i) instanceof Conversation) {
					children.remove(i);
				}
			}
		}
		
		Collections.sort(children, new BurdeeObjectNameComparator<BurdeeObject>());
		return children.get(index);
	}

	@Override
	public int getChildCount(Object parent) {
		if (parent instanceof ContactCategory) {
			return ((ContactCategory) parent).getContacts().size();
		} else if (Account.class.isAssignableFrom(parent.getClass())) {
			return ((Account) parent).getChildren().size() - 
				((Account) parent).getChildren(Conversation.class).size();
		} else {
			return ((BurdeeObject) parent).getChildren().size();
		}
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if (parent == null || child == null || parent instanceof Conversation ||
				child instanceof Conversation ||
				!(((BurdeeObject) parent).getChildren().contains(child) ||
						(parent instanceof ContactCategory && 
								((ContactCategory) parent).getContacts().contains(child)))) {
			return -1;
		}
		
		List<? extends BurdeeObject> children;
		if (parent instanceof ContactCategory && child instanceof Contact) {
			children = ((ContactCategory) parent).getContacts();
		} else {
			children = ((BurdeeObject) parent).getChildren();
		}
		
		children = new ArrayList<BurdeeObject>(children);
		Collections.sort(children, new BurdeeObjectNameComparator<BurdeeObject>());
		return children.indexOf(child);
	}

	@Override
	public BurdeeRoot getRoot() {
		return root;
	}

	@Override
	public boolean isLeaf(Object node) {
		return node instanceof Contact;
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		treeModelListeners.remove(l);
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		if (path.getLastPathComponent() != newValue) {
			TreeModelEvent event = new TreeModelEvent(this, path);
			for (TreeModelListener l : treeModelListeners) {
				l.treeNodesChanged(event);
			}
		}
	}

}
