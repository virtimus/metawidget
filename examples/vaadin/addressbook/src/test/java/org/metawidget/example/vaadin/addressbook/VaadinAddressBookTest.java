// Metawidget
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package org.metawidget.example.vaadin.addressbook;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import junit.framework.TestCase;

import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;
import org.metawidget.example.shared.addressbook.model.ContactType;
import org.metawidget.example.shared.addressbook.model.Gender;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.vaadin.Facet;
import org.metawidget.vaadin.VaadinMetawidget;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickShortcut;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Loghman Barari
 */

public class VaadinAddressBookTest
	extends TestCase {

	//
	// Public methods
	//

	@SuppressWarnings( "deprecation" )
	public void testAddressBook()
		throws Exception {

		// Start app

		AddressBook addressBook = new AddressBook();
		CustomLayout customLayout = (CustomLayout) addressBook.getContent();
		assertEquals( customLayout.getTemplateName(), "addressbook" );
		VerticalLayout contactsMainBody = (VerticalLayout) customLayout.getComponent( "pagebody" );
		assertEquals( contactsMainBody.getComponentCount(), 2 );
		Table contactsTable = (Table) contactsMainBody.getComponent( 1 );
		assertEquals( contactsTable.getContainerDataSource().getItemIds().size(), 6 );

		// Check searching

		VaadinMetawidget metawidgetSearch = (VaadinMetawidget) contactsMainBody.getComponent( 0 );

		( (TextField) metawidgetSearch.getComponent( "surname" ) ).setValue( "Simpson" );
		( (ComboBox) metawidgetSearch.getComponent( "type" ) ).setValue( ContactType.PERSONAL );

		FormLayout layout = metawidgetSearch.getContent();
		assertEquals( 4, layout.getComponentCount() );
		Facet buttons = (Facet) layout.getComponent( 3 );
		assertEquals( buttons.getWidth(), 100f );
		HorizontalLayout buttonsLayout = ( (VaadinMetawidget) ( (VerticalLayout) buttons.getContent() ).getComponent( 0 ) ).getContent();
		Button buttonSearch = (Button) buttonsLayout.getComponent( 0 );
		assertEquals( "Search", buttonSearch.getCaption() );
		assertEquals( "Add Personal Contact", ( (Button) buttonsLayout.getComponent( 1 ) ).getCaption() );
		assertEquals( "Add Business Contact", ( (Button) buttonsLayout.getComponent( 2 ) ).getCaption() );

		clickButton( buttonSearch );
		assertEquals( ContactType.PERSONAL, ( (ComboBox) metawidgetSearch.getComponent( "type" ) ).getValue() );

		ContactsController contactsController = addressBook.getContactsController();
		assertEquals( 2, contactsController.getAllByExample( (ContactSearch) metawidgetSearch.getToInspect() ).size() );

		// Open dialog for Personal Contact

		Contact contact = contactsController.load( 1 );
		assertEquals( "Mr Homer Simpson", contact.getFullname() );
		assertEquals( "Mr Homer Simpson", contact.toString() );
		assertEquals( 32, contact.hashCode() );
		assertFalse( contact.equals( new Object() ) );
		assertEquals( contact, contact );
		assertEquals( contact.compareTo( null ), -1 );
		assertEquals( contact.compareTo( contact ), 0 );
		assertEquals( "742 Evergreen Terrace", contact.getAddress().getStreet() );
		assertEquals( 1, contact.getCommunications().size() );

		ContactDialog contactDialog = new ContactDialog( addressBook, contact );

		// Check loading

		Iterator<Component> i = ( (CustomLayout) ( (VerticalLayout) contactDialog.getContent() ).getComponent( 0 ) ).getComponentIterator();
		i.next();
		VaadinMetawidget metawidgetContact = (VaadinMetawidget) i.next();
		assertEquals( "Homer", ( (Label) metawidgetContact.getComponent( "firstname" ) ).getValue() );
		assertEquals( Gender.MALE, ( (Label) metawidgetContact.getComponent( "gender" ) ).getValue() );
		layout = (FormLayout) metawidgetContact.getContent();
		assertEquals( "Contact Details", ( (Label) layout.getComponent( 5 ) ).getValue() );
		assertEquals( new Date( 56, Calendar.MAY, 12 ), ( (Label) metawidgetContact.getComponent( "dateOfBirth" ) ).getValue() );

		try {
			metawidgetContact.getComponent( "bad-value" );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			// Should throw MetawidgetException
		}

		VerticalLayout communicationsWrapper = metawidgetContact.getComponent( "communications" );
		Table table = (Table) communicationsWrapper.getComponent( 0 );
		@SuppressWarnings( "unchecked" )
		TableDataSource<Communication> dataSource = (TableDataSource<Communication>) table.getContainerDataSource();

		assertEquals( 1, dataSource.getItemIds().size() );
		assertEquals( ( (FormLayout) metawidgetContact.getContent() ).getComponentCount(), 11 );

		// Read-only editing does nothing

		assertTrue( !table.isEditable() );
		assertTrue( !table.isSelectable() );

		// Check editing

		buttons = (Facet) layout.getComponent( layout.getComponentCount() - 1 );
		buttonsLayout = ( (VaadinMetawidget) ( (VerticalLayout) buttons.getContent() ).getComponent( 0 ) ).getContent();
		Button editButton = (Button) buttonsLayout.getComponent( 0 );
		assertEquals( "Edit", editButton.getCaption() );
		clickButton( editButton );

		assertEquals( Gender.MALE, ( (ComboBox) metawidgetContact.getComponent( "gender" ) ).getValue() );
		( (TextField) metawidgetContact.getComponent( "surname" ) ).setValue( "Sapien" );
		layout = (FormLayout) metawidgetContact.getContent();
		assertEquals( "Contact Details", ( (Label) layout.getComponent( 5 ) ).getValue() );
		assertEquals( layout.getComponentCount(), 11 );

		// Check editing a communication

		communicationsWrapper = metawidgetContact.getComponent( "communications" );
		table = (Table) communicationsWrapper.getComponent( 0 );
		assertTrue( !table.isEditable() );
		assertTrue( table.isSelectable() );
		table = (Table) communicationsWrapper.getComponent( 0 );

		@SuppressWarnings( "unchecked" )
		TableDataSource<Communication> tableDataSource = (TableDataSource<Communication>) table.getContainerDataSource();
		Communication communication = tableDataSource.getDataRow( 1 );
		CommunicationDialog communicationDialog = new CommunicationDialog( contactDialog, communication );

		VaadinMetawidget communicationMetawidget = (VaadinMetawidget) ( (VerticalLayout) communicationDialog.getContent() ).getComponent( 0 );
		assertEquals( "Telephone", ( (ComboBox) communicationMetawidget.getComponent( "type" ) ).getValue() );
		assertEquals( "(939) 555-0113", ( (TextField) communicationMetawidget.getComponent( "value" ) ).getValue() );

		// Check adding a communication

		communication = new Communication();
		communicationDialog = new CommunicationDialog( contactDialog, communication );

		communicationMetawidget = (VaadinMetawidget) ( (VerticalLayout) communicationDialog.getContent() ).getComponent( 0 );
		( (ComboBox) communicationMetawidget.getComponent( "type" ) ).setValue( "Mobile" );
		( (TextField) communicationMetawidget.getComponent( "value" ) ).setValue( "(0402) 123 456" );

		layout = (FormLayout) communicationMetawidget.getContent();
		buttons = (Facet) layout.getComponent( layout.getComponentCount() - 1 );
		buttonsLayout = (HorizontalLayout) ( (VerticalLayout) buttons.getContent() ).getComponent( 0 );
		Button saveButton = (Button) buttonsLayout.getComponent( 0 );
		assertEquals( "Save", saveButton.getCaption() );
		clickButton( saveButton );

		assertEquals( 2, dataSource.getItemIds().size() );

		// Check deleting a communication

		buttonsLayout = (HorizontalLayout) communicationsWrapper.getComponent( 1 );
		Button deleteButton = (Button) buttonsLayout.getComponent( 1 );
		assertEquals( "Delete", deleteButton.getCaption() );
		assertTrue( !deleteButton.isEnabled() );
		table.select( 2 );
		deleteButton.setEnabled( true );
		clickButton( deleteButton );

		assertEquals( 1, dataSource.getItemIds().size() );

		// Check saving

		try {
			( (PopupDateField) metawidgetContact.getComponent( "dateOfBirth" ) ).setValue( "foo" );
			assertTrue( false );
		} catch ( Exception e ) {
			assertEquals( "Date format not recognized", e.getMessage() );
		}

		( (PopupDateField) metawidgetContact.getComponent( "dateOfBirth" ) ).setValue( new Date( 57, Calendar.MAY, 12 ) );
		layout = (FormLayout) metawidgetContact.getContent();
		buttons = (Facet) layout.getComponent( layout.getComponentCount() - 1 );
		buttonsLayout = ( (VaadinMetawidget) ( (VerticalLayout) buttons.getContent() ).getComponent( 0 ) ).getContent();
		saveButton = (Button) buttonsLayout.getComponent( 0 );
		assertEquals( "Save", saveButton.getCaption() );
		assertEquals( "Simpson", contact.getSurname() );
		clickButton( saveButton );

		assertEquals( "Sapien", contact.getSurname() );
		assertEquals( new Date( 57, Calendar.MAY, 12 ), ( (PopupDateField) metawidgetContact.getComponent( "dateOfBirth" ) ).getValue() );

		Iterator<Communication> iterator = contact.getCommunications().iterator();
		communication = iterator.next();
		assertEquals( "Mobile", communication.getType() );
		assertEquals( "(0402) 123 456", communication.getValue() );
		assertFalse( iterator.hasNext() );

		// Check re-viewing

		contactDialog = new ContactDialog( addressBook, contact );
		i = ( (CustomLayout) ( (VerticalLayout) contactDialog.getContent() ).getComponent( 0 ) ).getComponentIterator();
		i.next();
		metawidgetContact = (VaadinMetawidget) i.next();
		assertEquals( "Sapien", ( (Label) metawidgetContact.getComponent( "surname" ) ).getValue() );
		assertEquals( new Date( 57, Calendar.MAY, 12 ), ( (Label) metawidgetContact.getComponent( "dateOfBirth" ) ).getValue() );

		communicationsWrapper = metawidgetContact.getComponent( "communications" );
		table = (Table) communicationsWrapper.getComponent( 0 );
		assertEquals( 1, table.getItemIds().size() );

		// Search everything

		( (TextField) metawidgetSearch.getComponent( "surname" ) ).setValue( null );
		( (ComboBox) metawidgetSearch.getComponent( "type" ) ).setValue( null );
		clickButton( buttonSearch );
		assertEquals( 6, contactsController.getAllByExample( (ContactSearch) metawidgetSearch.getToInspect() ).size() );

		// Open dialog for Business Contact

		contact = contactsController.load( 5 );
		assertEquals( "Mr Charles Montgomery Burns", contact.getFullname() );
		contactDialog = new ContactDialog( addressBook, contact );
		i = ( (CustomLayout) ( (VerticalLayout) contactDialog.getContent() ).getComponent( 0 ) ).getComponentIterator();
		i.next();
		metawidgetContact = (VaadinMetawidget) i.next();
		layout = (FormLayout) metawidgetContact.getContent();
		buttons = (Facet) layout.getComponent( layout.getComponentCount() - 1 );
		buttonsLayout = ( (VaadinMetawidget) ( (VerticalLayout) buttons.getContent() ).getComponent( 0 ) ).getContent();
		Button backButton = (Button) buttonsLayout.getComponent( 1 );
		assertEquals( "Back", backButton.getCaption() );
		editButton = (Button) buttonsLayout.getComponent( 0 );
		assertEquals( "Edit", editButton.getCaption() );
		clickButton( editButton );

		assertEquals( "Charles Montgomery", ((TextField) metawidgetContact.getComponent( "firstname" )).getValue() );
		assertEquals( Gender.MALE, ( (ComboBox) metawidgetContact.getComponent( "gender" ) ).getValue() );
		assertEquals( "0", ( (TextField) metawidgetContact.getComponent( "numberOfStaff" ) ).getValue() );
	}

	//
	// Private methods
	//

	private void clickButton( Button button ) {

		new ClickShortcut( button, "" ).handleAction( null, null );
	}
}
