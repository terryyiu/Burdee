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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import ca.burdee.object.BurdeeObject;
import ca.burdee.object.BurdeeRoot;
import ca.burdee.swing.BurdeeFrame;

/**
 * The main Burdee chat client application to execute to initialize the program.
 */
public class BurdeeApplication {
	
	private static final Logger logger = Logger.getLogger(BurdeeApplication.class.getName());

	/**
	 * The {@link BurdeeRoot} object that is the top ancestor
	 * {@link BurdeeObject} of the entire hierarchy object model tree.
	 */
	private static final BurdeeRoot root = new BurdeeRoot();

	/**
	 * This main method initializes the Burdee application. The look and feel of
	 * the UI is set to be like the system's native look and feel. There are
	 * also Mac OS X specific hooks that place the menubar on the top of the
	 * screen like any other Mac OS X application.
	 * 
	 * @param args
	 *            The arguments passed in when executing the application.
	 */
	public static void main(final String[] args) {
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Burdee");
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// If setting the UI to the operating system's look and feel fails,
			// squish the exception and continue using Java's default look and feel.
			logger.log(Level.CONFIG, 
					"Unable to set UI to native look and feel; " +
					"continuing with default Java look and feel.", e);
		}
		
		// Schedules a job for the event-dispatching thread to
		// create and show the main GUI.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

	/**
	 * Creates and shows the {@link BurdeeFrame} main GUI. This method should
	 * be called by the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		BurdeeFrame frame = new BurdeeFrame(root);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}
