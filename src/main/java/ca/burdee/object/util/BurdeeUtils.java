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

package ca.burdee.object.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.burdee.object.BurdeeListener;
import ca.burdee.object.BurdeeObject;

/**
 * This utility class contains a collection of static methods that are useful
 * and generic enough to be used by multiple classes.
 */
public class BurdeeUtils {

	/**
	 * This class is a utility class containing all static methods. Do not
	 * create an instance of this class.
	 */
	private BurdeeUtils() {
	}

	/**
	 * Gets an unmodifiable {@link List} of ancestors, where the highest
	 * ancestor appears first, and the passed in descendant object appears last.
	 * 
	 * @param descendant
	 *            The descendant {@link BurdeeObject} to retrieve its ancestor
	 *            {@link List} from.
	 * @return The unmodifiable {@link List} of {@link BurdeeObject} ancestors.
	 */
	public static List<BurdeeObject> getAncestorList(BurdeeObject descendant) {
		List<BurdeeObject> ancestorList = new ArrayList<BurdeeObject>();
		BurdeeObject ancestor = descendant;
		while (ancestor != null) {
			ancestorList.add(0, ancestor);
			ancestor = ancestor.getParent();
		}
		return Collections.unmodifiableList(ancestorList);
	}

	/**
	 * Gets the top ancestor {@link BurdeeObject} of a given
	 * {@link BurdeeObject} descendant.
	 * 
	 * @param descendant
	 *            The descendant {@link BurdeeObject} whose ancestor hierarchy
	 *            should be walked up to find the top ancestor.
	 * @return The top ancestor {@link BurdeeObject}. If there is none, null is
	 *         returned.
	 */
	public static BurdeeObject getAncestor(BurdeeObject descendant) {
		BurdeeObject ancestor = descendant.getParent();
		while (ancestor != null) {
			ancestor = ancestor.getParent();
		}
		return ancestor;
	}

	/**
	 * Gets the first ancestor {@link BurdeeObject} of a given
	 * {@link BurdeeObject} descendant that matches a specific
	 * {@link BurdeeObject} type.
	 * 
	 * @param descendant
	 *            The descendant {@link BurdeeObject} to find the ancestor from.
	 * @param clazz
	 *            The {@link BurdeeObject} {@link Class} to find.
	 * @return The first ancestor {@link BurdeeObject} of the given type.
	 */
	public static BurdeeObject getAncestor(BurdeeObject descendant, 
			Class<? extends BurdeeObject> clazz) {
		BurdeeObject ancestor = descendant.getParent();
		while (ancestor != null && !clazz.isAssignableFrom(ancestor.getClass())) {
			ancestor = ancestor.getParent();
		}
		return ancestor;
	}

	/**
	 * Adds a {@link BurdeeListener} to a {@link BurdeeObject} and all of its
	 * descendants.
	 * 
	 * @param bo
	 *            The {@link BurdeeObject} to add the {@link BurdeeListener} to
	 *            its hierarchy.
	 * @param l
	 *            The {@link BurdeeListener} to add.
	 */
	public static void listenToHierarchy(BurdeeObject bo, BurdeeListener l) {
		bo.addBurdeeListener(l);
		for (BurdeeObject child : bo.getChildren()) {
			listenToHierarchy(child, l);
		}
	}

	/**
	 * Removes a {@link BurdeeListener} from a {@link BurdeeObject} and all of
	 * its descendants.
	 * 
	 * @param bo
	 *            The {@link BurdeeObject} to remove the {@link BurdeeListener}
	 *            from its hierarchy.
	 * @param l
	 *            The {@link BurdeeListener} to remove.
	 */
	public static void unlistenFromHierarchy(BurdeeObject bo, BurdeeListener l) {
		bo.removeBurdeeListener(l);
		for (BurdeeObject child : bo.getChildren()) {
			unlistenFromHierarchy(child, l);
		}
	}

	/**
	 * Determines if two objects are equal, even if either of them are null.
	 * 
	 * @param o1
	 *            The first object to equate. This value can be null.
	 * @param o2
	 *            The second object to equate. This value can be null.
	 * @return true if the two objects are equal.
	 */
	public static boolean isNullSafeEquals(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		} else if (o1 == null || o2 == null) {
			return false;
		} else {
			return o1.equals(o2);
		}
	}
	
}
