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

package ca.burdee;

import ca.burdee.swing.AboutDialog;

/**
 * This class keeps track of the application version number. This is useful for
 * display the version number in the {@link AboutDialog}, and in the future,
 * running builds and packaging installers.
 */
public class BurdeeVersion {

	/**
	 * The major portion of the version number. This takes the highest
	 * precedence of the version number.
	 */
	public static final String MAJOR_VERSION = "0";

	/**
	 * The minor portion of the version number. This takes the next highest
	 * precedence of the version number after {@link #MAJOR_VERSION}.
	 */
	public static final String MINOR_VERSION = "1";

	/**
	 * The patch portion of the version number. This takes the lowest precedence
	 * of the version number.
	 */
	public static final String PATCH_VERSION = "0";

	/**
	 * This is a class full of static variables and methods. Do not create an
	 * instance of it.
	 */
	private BurdeeVersion() {}

	/**
	 * Gets the version number of the application. This appends the
	 * {@link #MAJOR_VERSION}, {@link #MINOR_VERSION}, and
	 * {@link #PATCH_VERSION} values together into one string.
	 * 
	 * @return The whole version number.
	 */
	public static String getVersionNumber() {
		return MAJOR_VERSION + "." + MINOR_VERSION + "." + PATCH_VERSION;
	}

}
