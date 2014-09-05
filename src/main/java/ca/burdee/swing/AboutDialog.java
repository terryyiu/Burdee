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

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;
import ca.burdee.BurdeeVersion;

/**
 * This About {@link JDialog} displays information about the application, such
 * as the project name, copyright notice, and credits.
 */
public class AboutDialog extends JDialog {

	/**
	 * This {@link ImageIcon} is used as this dialog's icon.
	 */
	public static final ImageIcon ABOUT_ICON = 
		new ImageIcon(BurdeeFrame.class.getResource("/icons/information.png"));
	
	/**
	 * The name of this application.
	 */
	private static final String APPLICATION_NAME = "Burdee";
	
	/**
	 * A short description of what the program does.
	 */
	private static final String APPLICATION_DESCRIPTION = "An Instant Messaging Client";
	
	/**
	 * Copyright notice.
	 */
	private static final String COPYRIGHT_NOTICE = "Copyright (C) 2010 Terry Yiu";

	/**
	 * Credits to people who created the external libraries used in this
	 * application, or anyone who helped in general.
	 */
	private static final String[] CREDITS = {
		"Smack - Jive Software",
		"MiGLayout - MiG InfoCom AB",
		"famfamfam Silk Icons - http://www.famfamfam.com/lab/icons/silk/"
	};

	/**
	 * Creates a new About {@link JDialog} that displays information about this
	 * application.
	 * 
	 * @param owner
	 *            The {@link Frame} from which this {@link JDialog} is
	 *            displayed.
	 */
	public AboutDialog(Frame owner) {
		super(owner, "About Burdee");
		buildUI();
	}
	
	/**
	 * Builds the UI.
	 */
	private void buildUI() {
		setLayout(new MigLayout("", "[center]", "[][][][][fill,grow]"));
		
		add(new JLabel("<html><font size=\"5\"><b>" + 
				APPLICATION_NAME + " " + BurdeeVersion.getVersionNumber() + 
				"</b></font></html>"), "span");
		
		add(new JLabel("<html><i>" + APPLICATION_DESCRIPTION + "</i></html>"), "span");
		
		add(new JLabel(COPYRIGHT_NOTICE), "span");
		
		add(new JLabel("Credits:"), "span, left");
		
		StringBuilder sb = new StringBuilder();
		for (String credit : CREDITS) {
			if (sb.length() > 0) {
				sb.append("\n");
			}
			sb.append(credit);
		}
		JTextArea textArea = new JTextArea(sb.toString());
		textArea.setEditable(false);
		
		add(new JScrollPane(textArea), "span");
		
		setIconImage(ABOUT_ICON.getImage());
		setMinimumSize(new Dimension(400, 300));
	}

}
