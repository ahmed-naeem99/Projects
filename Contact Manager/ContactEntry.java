/*
 * Name: Ahmed Naeem
 * Date: March 20, 2024
 * Description: This class represents a contact entry with fields for first name, last name, address, phone number, and email. 
 * 				It provides methods to set and retrieve these fields, as well as to retrieve formatted contact details and records. 
 * 				Additionally, it includes methods to compare contact entries based on first name and phone number.
 * 
 * Input: Input for this class typically includes data such as first name, last name, 
 * 		  address, phone number, and email address, which are used to create and manipulate contact entries.
 * 
 * Output: The output includes formatted contact details and records, which can be used for display or storage purposes. 
 * 		   Additionally, certain methods return boolean values indicating comparisons between contact entries, 
 * 		   such as whether one entry has a greater first name or phone number compared to another.
 */

package helloWorld;

import java.util.Comparator;

public class ContactEntry {
	private String firstName;
	private String lastName;
	private String address;
	private String phoneNumber;
	private String email;

    // Constructor to initialize contact entry fields
	public ContactEntry(String firstName, String lastName, String address, String phoneNumber, String email) {
		setFirstName(firstName);
		setLastName(lastName);
		setAddress(address);
		setPhoneNumber(phoneNumber);
		setEmail(email);
	}

	/*
	 * The following are setters and getters for setting and retrieving the fields
	 */
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

    // Method to retrieve formatted contact details for deleting the contacts
	public String getContactDetails() {
		return "First Name: " + getFirstName() + "\n" + "Last Name: " + getLastName() + "\n" + "Address: "
				+ getAddress() + "\n" + "Phone Number: " + getPhoneNumber() + "\n" + "Email: " + getEmail();
	}
	
	
	// Method to convert contact details to a row for the table model. 
	public String[] toTableRow() {
		return new String[] {firstName, lastName, address, phoneNumber, email};
	}
	
	// Returns a string representation of the contact entry.
	@Override
	public String toString() {
		    return getContactRecord(",");
	}
	
    // Method to retrieve contact record with specified separator
	public String getContactRecord(String separator) {
		
		return getFirstName() + separator + getLastName() + separator + getAddress() + separator + getPhoneNumber() + separator + getEmail();
	}

	/*
	 * The following methods are for sorting the contacts.
	 * They check if the name or number before them is greater (A-Z or 9-0)
	 * These methods work if they choose either ascending or descending
	 */
	
	public boolean nameGreaterThan(ContactEntry otherContactEntry) {
		
		return (getFirstName().compareTo(otherContactEntry.getFirstName()) > 0);

	}

	public boolean numberGreaterThan(ContactEntry otherContactEntry) {
		
		return (getPhoneNumber().compareTo(otherContactEntry.getPhoneNumber()) > 0);
	}
}

/*
 * The following are the comparators used for specifying the sort method based on either name or number
 */

class ContactComparatorByName implements Comparator<ContactEntry> { 
	  
    // override the compare() method 
    public int compare(ContactEntry c1, ContactEntry c2) { 
            return c1.getFirstName().compareTo(c2.getFirstName()); 
    } 
}

class ContactComparatorByNumber implements Comparator<ContactEntry> { 
	  
    // override the compare() method 
    public int compare(ContactEntry c1, ContactEntry c2) { 
            return c1.getPhoneNumber().compareTo(c2.getPhoneNumber()); 
    } 
}

