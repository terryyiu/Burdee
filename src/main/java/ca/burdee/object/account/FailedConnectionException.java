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

package ca.burdee.object.account;

/**
 * This {@link Exception} indicates that an {@link Account} that attempted to
 * connect to its IM service failed.
 */
public class FailedConnectionException extends Exception {

	/**
	 * Creates a new {@link FailedConnectionException} with no detailed message
	 * and no cause.
	 */
	public FailedConnectionException() {
		super();
	}

	/**
	 * Creates a new {@link FailedConnectionException} with a detailed message
	 * but no cause.
	 * 
	 * @param message
	 *            The detailed message of the {@link Exception}.
	 */
	public FailedConnectionException(String message) {
		super(message);
	}

	/**
	 * Creates a new {@link FailedConnectionException} with a {@link Throwable}
	 * cause but no detailed message.
	 * 
	 * @param cause
	 *            The {@link Throwable} cause of the {@link Exception}.
	 */
	public FailedConnectionException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new {@link FailedConnectionException} with a detailed message
	 * and a {@link Throwable} cause.
	 * 
	 * @param message
	 *            The detailed message of the {@link Exception}.
	 * @param cause
	 *            The {@link Throwable} cause of the {@link Exception}.
	 */
	public FailedConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

}
