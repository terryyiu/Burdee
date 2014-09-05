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

import java.util.Comparator;

/**
 * This {@link Comparator} compares {@link BurdeeObject}s by their UUID. Null
 * objects can be compared.
 * 
 * @param <T>
 *            The type of {@link BurdeeObject} that is being compared.
 */
public class BurdeeObjectUUIDComparator<T extends BurdeeObject> implements Comparator<T> {

	@Override
	public int compare(T o1, T o2) {
		if (o1 == o2) {
			return 0;
		} else if (o1 == null) {
			return -1;
		} else if (o2 == null) {
			return 1;
		} else {
			return o1.getUuid().compareTo(o2.getUuid());
		}
	}

}