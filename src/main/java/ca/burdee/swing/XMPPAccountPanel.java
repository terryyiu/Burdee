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

import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import ca.burdee.object.account.Account;
import ca.burdee.object.account.Protocol;
import ca.burdee.object.account.XMPPAccount;

/**
 * This {@link AccountPanel} modifies an {@link XMPPAccount}.
 */
public class XMPPAccountPanel<T extends XMPPAccount> extends AccountPanel<T> {
	
	/**
	 * The {@link JTextField} that defines the domain name of the
	 * {@link Account}.
	 */
	private JTextField domainTextField;

	/**
	 * The {@link JTextField} that defines the resource name of this
	 * {@link Account}.
	 */
	private JTextField resourceTextField;

	/**
	 * Creates a new {@link XMPPAccountPanel}.
	 * 
	 * @param account
	 *            The {@link XMPPAccount} that this panel modifies.
	 * @param protocolListener
	 *            The {@link ItemListener} that listens to changes in selection
	 *            for the {@link Protocol} {@link JComboBox}.
	 */
	public XMPPAccountPanel(T account, ItemListener protocolListener) {
		super(account, protocolListener);
	}
	
	@Override
	protected void buildUI() {
		super.buildUI();
		buildDomainTextField();
		buildResourceTextField();
	}
	
	/**
	 * Builds the {@link XMPPAccount} domain name text field and adds it to the
	 * panel.
	 */
	 private void buildDomainTextField() {
		 domainTextField = new JTextField();
		 domainTextField.setText(account.getDomain());
		 domainTextField.getDocument().addDocumentListener(new DocumentListener() {

			 @Override
			 public void removeUpdate(DocumentEvent e) {
				 updateDomain(e.getDocument());
			 }

			 @Override
			 public void insertUpdate(DocumentEvent e) {
				 updateDomain(e.getDocument());
			 }

			 @Override
			 public void changedUpdate(DocumentEvent e) {
				 updateDomain(e.getDocument());
			 }

			 private void updateDomain(Document document) {
				 try {
					 account.setDomain(document.getText(0, document.getLength()));
				 } catch (BadLocationException e) {
					 throw new RuntimeException(e);
				 }
			 }

		 });
		 
		 add(new JLabel("Domain"), "span");
		 add(domainTextField, "span");
	 }

	/**
	 * Builds the {@link XMPPAccount} resource text field and adds it to the
	 * panel.
	 */
	 private void buildResourceTextField() {
		 resourceTextField = new JTextField();
		 resourceTextField.setText(account.getResource());
		 resourceTextField.getDocument().addDocumentListener(new DocumentListener() {

			 @Override
			 public void removeUpdate(DocumentEvent e) {
				 updateResource(e.getDocument());
			 }

			 @Override
			 public void insertUpdate(DocumentEvent e) {
				 updateResource(e.getDocument());
			 }

			 @Override
			 public void changedUpdate(DocumentEvent e) {
				 updateResource(e.getDocument());
			 }

			 private void updateResource(Document document) {
				 try {
					 account.setResource(document.getText(0, document.getLength()));
				 } catch (BadLocationException e) {
					 throw new RuntimeException(e);
				 }
			 }

		 });
		 
		 add(new JLabel("Resource"), "span");
		 add(resourceTextField, "span");
	 }

}
