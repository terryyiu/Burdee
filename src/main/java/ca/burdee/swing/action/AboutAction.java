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

package ca.burdee.swing.action;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;

import ca.burdee.swing.AboutDialog;

/**
 * This {@link Action} opens the {@link AboutDialog}. If it is already opened,
 * then focus will be given to the dialog.
 */
public class AboutAction extends AbstractAction {
	
	/**
	 * The {@link AboutDialog} reference.
	 */
	private AboutDialog aboutDialog;

	/**
	 * The {@link Frame} owner that creates and performs this action.
	 */
	private final Frame owner;

	/**
	 * Creates a new {@link AboutAction}.
	 * 
	 * @param owner
	 *            The {@link Frame} owner that creates and can perform this
	 *            action.
	 */
	public AboutAction(Frame owner) {
		super("About Burdee");
		this.owner = owner;
	}

	/**
	 * Creates and shows a new {@link AboutDialog} if it does not exist. Focus
	 * is given to the dialog if it already exists. There can only ever be one
	 * instance of the dialog open at one time.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (aboutDialog == null) {
			aboutDialog = new AboutDialog(owner);
			aboutDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			aboutDialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					aboutDialog.removeWindowListener(this);
					aboutDialog = null;
				}
			});
		}
		aboutDialog.setVisible(true);
	}

}
