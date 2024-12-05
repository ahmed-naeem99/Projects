/*
 * Description: This program implements a Contacts class to manage a list of contact entries. 
 * 				This class encapsulates an ArrayList of ContactEntry objects and makes it possible for 
 * 				ContactManagerV4 to be able to use any form of collection for the contacts
 * 
 * Input: ContactEntry objects to be added or deleted.
 * 		  A search criteria (String) to search for a contact by first name or phone number.
 * 		  Sort order for sorting contacts by name or number.
 * 
 * Output: For searchContact(): A ContactEntry object matching the search criteria, if found.
 * 		   For sortContactsByName() and sortContactsByNumber(): Contacts sorted by name or number.
 * 		   For writeAllContacts(): Prints all contacts to a PrintWriter.
 * 		   For getContactsAsStringArray(): Returns contacts as a 2D String array.
 */

package helloWorld;

import java.io.PrintWriter;
import java.util.ArrayList;

public class Contacts {

    public static final int ASCENDING_ORDER = 1;
    public static final int DESCENDING_ORDER = 2;

    // ArrayList to store contact entries
    private ArrayList<ContactEntry> entries = new ArrayList<>();

    // Method to add a contact to the list
    public void addContact(ContactEntry contact) {
        entries.add(contact);
    }

    // Method to delete a contact from the list
    public void deleteContact(ContactEntry contact) {
        entries.remove(contact);
    }

    // Method to search for a contact by first name or phone number
    public ContactEntry searchContact(String searchCriteria) {
        // Default set to not found
        ContactEntry foundContact = null;
        for (int i = 0; i < entries.size(); i++) {
            ContactEntry entry = entries.get(i);
            // Check if the given choice matches the first name or phone number of the contact
            if (entry.getFirstName().equalsIgnoreCase(searchCriteria) || entry.getPhoneNumber().equals(searchCriteria)) {
                // Return the index if found
                foundContact = entries.get(i);
                break;
            }
        }
        return foundContact;
    }

    // Method to sort contacts by name
    public void sortContactsByName(int sortOrder) {
    	
    	/*
    	 * Comparator is used from the ContactEntry class.
    	 * Uses built in sort method, and .reversed() to give a backwards sort functions 
    	 */
        ContactComparatorByName nameComparator = new ContactComparatorByName();
        if (sortOrder == ASCENDING_ORDER) {
            entries.sort(nameComparator);
        } else {
            entries.sort(nameComparator.reversed());
        }
    }

    // Method to sort contacts by phone number
    public void sortContactsByNumber(int sortOrder) {
    	
    	/*
    	 * Comparator is used from the ContactEntry class.
    	 * Uses built in sort method, and .reversed() to give a backwards sort functions 
    	 */
        ContactComparatorByNumber numberComparator = new ContactComparatorByNumber();
        if (sortOrder == ASCENDING_ORDER) {
            entries.sort(numberComparator);
        } else {
            entries.sort(numberComparator.reversed());
        }
    }

    // Method to write all contacts to a PrintWriter
    public void writeAllContacts(PrintWriter writer) {
        for (ContactEntry entry : entries) {
            writer.println(entry);
        }
    }

    // Method to retrieve contacts as a 2D String array
    public String[][] getContactsAsStringArray() {
        String[][] contactsAsStringArray = new String[entries.size()][5];
        for (int i = 0; i < entries.size(); i++) {
            ContactEntry contact = entries.get(i);
            String[] contactFields = contact.toTableRow();
            for (int j = 0; j < contactFields.length; j++) {
                contactsAsStringArray[i][j] = contactFields[j];
            }
        }
        return contactsAsStringArray;
    }

    // Method to clear all contacts from the list
    public void clearContacts() {
        entries.clear();
    }
}
