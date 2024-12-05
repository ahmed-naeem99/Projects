/*
 * Name: Ahmed Naeem
 * Date: October 9, 2023
 * Description: Make a contact book that allows the user to do the following operations from a main menu:
 * 	add an entry
 * 	view all entries
 * 	modify an entry
 * 	delete an entry
 * 	save the contact book
 * 	load the contact book
 *  sort contacts
 *  search for a contact
 * 	quit
 * Input: Name/number to input, name/number to search to delete/modify/read
 * Output: ContactBook.txt, where all the contacts are stored.
 */


package helloWorld;

//Import relevant java IO libraries that allow program to execute
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;

/*
 * to-do
 */
public class ContactManagerV2 {

	//Initializing buffered reader for user input
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	/*
	 * String arrays containing contact information, and main contact book which adds both arrays together.
	 * Contact Book limit is 100 based on assignment instructions
	 */
	static String[] name = new String[100];
	static String[] number = new String[100];
	static String[] book = new String[100];

	//Initialize counter to control array limit
	static int count = 0;	

	/*
	 * Method that displays the main menu with all the options listed in the description of the program 
	 */
	public static void displayMenu() throws IOException {

		boolean quitProgram = false;

		while (!quitProgram) {
			try {
				System.out.print("\nMain Menu >> \n1. Add a contact entry \n2. View all contacts in book \n3. Modify a contact entry \n4. Delete a contact entry \n5. Save the contact book \n6. Load the contact book \n7. Sort contacts \n8. Search for a contact \n9. Quit \n\nEnter your choice: ");         

				//Read the input from the user and convert it into an integer for the switch method
				int choice = Integer.parseInt(in.readLine());

				/*
				 * Switch method that executes the program based on the choice the user gave from the main menu
				 * Option 1: add an entry
				 * Option 2: view all the entries
				 * Option 3: modify an entry
				 * Option 4: delete an entry
				 * Option 5: save the contact book
				 * Option 6: load the contact book
				 * Option 7: sort contacts
				 * Option 8: search contacts
				 * Option 9: quit
				 */

				switch (choice) {
				case 1:
					addEntry();
					break;
				case 2:
					viewEntries();
					break;
				case 3:
					modifyEntry();
					break;
				case 4:
					deleteEntry();
					break;
				case 5:
					saveBook();
					break;
				case 6:
					loadBook();
					break;
				case 7:
					sortContacts();
					break;
				case 8:
					searchContact();
					break;
				case 9:
					System.out.println("Quitting the program...Bye!");
					quitProgram = true;
					break;
				default:
					System.out.println("Invalid choice. Try again.");
				}

			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a valid integer choice.");

			} catch (IOException e) {
				System.out.println("Invalid choice. Try again.");		
			}
		}
	}

	/*
	 * Method that adds name and number from user input into one contact in the contact book 
	 */
	public static void addToContactBook() {

		for (int i = 0; i < book.length; i++) {
			addEntryToContactBook(i);  
		}
	}

	public static void addEntryToContactBook(int index) {
		book[index] = name[index] + "\t" + number[index]; 
	}


	/*
	 * Method that adds a contact to the Contact Book 
	 * Uses InputStreamReader to read name or number that user gives and adds it to the list
	 * Only adds the name or number if the count variable does not exceed the Contact Book array length
	 */
	public static void addEntry() throws IOException {

		try {
			//Checking if there is space in the contact book
			if (count < book.length) {

				//Ask user for input to add contact name to string array
				System.out.print("\nEnter the contact's name: ");
				name[count] = in.readLine();

				//Ask user for input to add contact number to string array			
				System.out.print("\nEnter the contact's number: ");
				number[count] = in.readLine();

				//Add counter by 1
				count++;

				//Call function to add name and number to one input in the contact book
				addToContactBook();

				System.out.print("\nEntry Added!\n");
			}

		} catch (IOException e) {

			//If contact book is full, tell user to delete a contact first
			System.out.println("The contact book is full. Try deleting an entry first!");
		}
	}

	/*
	 * Method that displays all the contacts that are in the Contact Book
	 * Uses a for loop and loops through every index of the string array and outputs it
	 */
	public static void viewEntries() throws IOException {

		for (int i = 0; i < count; i++) {
			System.out.println(book[i]);
		}
	}
	/*
	 * Method that modifies a contact thats in the Contact Book
	 * First searches the Contact Book by name or number
	 * Then gets user input for the new name
	 */
	public static void modifyEntry() throws IOException {

		try {
			//Set flag and boolean found to 0 and false because contact is not found yet
			int modifyContact = -1;

			System.out.print("\nWhich entry do you want to modify? Enter name or number: ");

			//Read user input
			String choice = in.readLine();

			for(int i = 0; i < book.length; i++) {

				//Look for a match in the name or number array, if found, set flag to index number, and found to true
				if (choice.equals(name[i]) || choice.equals(number[i])) {
					modifyContact = i;
					break;
				}
			}

			//When contact is found, get user input to modify contact
			if (modifyContact != -1) {

				System.out.print("\nEnter the new name: ");
				name[modifyContact] = in.readLine();

				System.out.print("\nEnter the new number: ");
				number[modifyContact] = in.readLine();

				//Call function to add name and number to one input in the contact book
				addEntryToContactBook(modifyContact);

				System.out.println("Entry has been modified");

			} else {

				System.out.println("Name/Number not found");
			}

		} catch (IOException e) {

			System.out.println(e.getMessage());
		}
	}

	/*
	 * Method that deletes a contact thats in the Contact Book
	 * First searches the Contact Book by name or number
	 * Then deletes the contact and shifts all the indexes under that contact up by one
	 */
	public static void deleteEntry() throws IOException {

		try {
			//Initialize flag to 0, in case user wants to delete first index
			int deletedContact = -1;

			System.out.print("Which entry do you want to delete? Enter name or number: ");
			String choice = in.readLine();

			for(int i = 0; i < book.length; i++) {

				//Look for contact in name or number array, if found, set flag to index number
				if (choice.equals(name[i]) || choice.equals(number[i])) {
					deletedContact = i;
					break;
				} 
			}

			if (deletedContact != -1) {

				//Shift all indexes under the deleted contact by 1 and then delete the contact	
				for (int i = deletedContact; i < count - 1; i++) {
					name[i] = name[i + 1];
					number[i] = number[i + 1];
					book[i] = book[i + 1];
				}

				name[count - 1] = null;
				number[count - 1] = null;
				book[count - 1] = null;

				count--; 

				System.out.println("Entry has been deleted");

			} else {
				System.out.println("Name/Number not found");
			}

		} catch (IOException e) {

			System.out.println(e.getMessage());
		}
	}
	/*
	 * Method that saves all the contacts into a file 
	 * Takes all added contacts in the book string array in case user wants to load file next program
	 */
	public static void saveBook() throws IOException {

		//Try/catch block to catch errors
		try {

			//Ask user for file name to save contacts in
			System.out.print("Which file do you want to save the contacts in?");

			//Read user input
			String loadFile = in.readLine();

			//Initialize file creator if file does not already exist
			File file = new File(loadFile);

			if (file.exists() == true) {
				file.createNewFile();
			}

			//To write all the contacts onto the file
			PrintWriter output = new PrintWriter(new FileWriter(file));

			System.out.println("Saving the file...");

			for (int i = 0; i < count; i++) {
				output.println(book[i]);
			}

			System.out.println("File saved!");

			output.close();

		} catch (IOException e) {

			System.out.println("An error occurred while saving the file: " + e.getMessage());

		} 
	}

	/*
	 * Method that loads the Contact Book 
	 * If Contact Book file does not exist, the catch statement creates one
	 */
	public static void loadBook() throws IOException {

		System.out.print("Which file do you want to load: ");

		//Read user input
		String loadFile = in.readLine();

		count = 0;

		BufferedReader reader = new BufferedReader(new FileReader(loadFile));

		try{

			System.out.println("Loading contacts...");

			//Reads saved file from a while loop until file is empty
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split("\t");
				if (parts.length == 2) {
					name[count] = parts[0];
					number[count] = parts[1];
					book[count] = line;
					count++;
				}
			}			
			//Tells user how many contacts were saved
			System.out.println("Loaded " + count + " contacts");

			//If Contact Book file does not exist, create it
		} catch (IOException e) {

			//If format in the file was invalid, tell the user
			System.out.println("File does not exist.");

		} finally {
			//Close file
			if (reader != null) {
				reader.close();
			}
		}
	}

	// Method that allows the user to choose a sorting criterion (name or number) and order (ascending or descending)
	public static void sortContacts() throws IOException {		
		try {
			// Display sorting options to the user
			System.out.println("Sort contacts by: ");
			System.out.println("1. Name");
			System.out.println("2. Number");
			System.out.print("Enter your choice: ");

			// Read user's sorting criterion choice
			int sortField = Integer.parseInt(in.readLine());

			// Based on the sorting criterion, display additional sorting options
			switch(sortField) {
			case 1:
				System.out.println("Sort by: ");
				System.out.println("1. A-Z");
				System.out.println("2. Z-A");
				System.out.print("Enter your choice: ");

				// Read user's additional sorting choice
				int sortByName = Integer.parseInt(in.readLine());

				sortContactsByName(sortByName);

				break;

			case 2:
				System.out.println("Sort by: ");

				/*
				 * Sorting 0-9 or 9-0 assuming all numbers are the same length,
				 * As per the V1 assignment instructions, the array that holds the numbers must be a String[] array,
				 * If it is a string array, the sorting works based on the first number, not the actual value on the number,
				 * If the numbers in the contact were to be an int[] array, you would simply have to change the String[] array to an int[] array to sort it based on the value
				 */
				System.out.println("1. 0-9");
				System.out.println("2. 9-0");
				System.out.print("Enter your choice: ");

				// Read user's additional sorting choice
				int sortByNumber = Integer.parseInt(in.readLine());

				sortContactsByNumber(sortByNumber);

				break;

			default:
				// Invalid input, prompt the user to try again and return to the main menu
				System.out.println("Invalid input. Try again");
				displayMenu();
			}			

			System.out.println("Contacts Sorted!");

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	// Method that performs ascending sorting based on contact names
	public static void sortContactsByName(int sortByName) {

		// Bubble sort algorithm for ascending order by name
		for (int i = 0; i < count - 1; i++) {
			for (int j = 0; j < count - i - 1; j++) {
				if (sortByName == 1) {
					if (name[j].compareTo(name[j + 1]) > 0) {
						// Swap contacts if they are out of order
						swap(j, j + 1);
					}
				} else if (sortByName == 2) {
					if (name[j].compareTo(name[j + 1]) < 0) {
						// Swap contacts if they are out of order
						swap(j, j + 1);
					}
				}
			}
		}
	}

	// Method that performs ascending sorting based on contact numbers
	public static void sortContactsByNumber(int sortByNumber) {

		// Bubble sort algorithm for ascending order by number
		for (int i = 0; i < count - 1; i++) {
			for (int j = 0; j < count - i - 1; j++) {
				if (sortByNumber == 1) {
					if (number[j].compareTo(number[j + 1]) > 0) {
						// Swap contacts if they are out of order
						swap(j, j + 1);
					}
				} else if (sortByNumber == 2) {
					if (number[j].compareTo(number[j + 1]) < 0) {
						// Swap contacts if they are out of order
						swap(j, j + 1);
					}
				}
			}
		}
	}

	// Method that swaps contacts at given indexes
	public static void swap(int i, int j) {
		String temp = book[i];
		book[i] = book[j];
		book[j] = temp;

		temp = name[i];
		name[i] = name[j];
		name[j] = temp;

		temp = number[i];
		number[i] = number[j];
		number[j] = temp;
	}


	/* 
	 * Method that searches for contacts based on user input 
	 * Searches for either name or number
	 * Displays all matching searches
	 */
	public static void searchContact() throws IOException {
		try {

			// Initialize an array to store found contacts to book length in case all of the entries have the same number/name
			String[] foundContacts = new String[book.length];
			int foundContactsCount = 0;

			// Prompt the user to enter a name or number to search
			System.out.print("Enter name (case-sensitive) or number to search: ");

			// Read user input
			String choice = in.readLine();

			// Iterate through the contact book to find matches
			for (int i = 0; i < book.length; i++) {

				// Check if the user input matches a name or number
				if (choice.equals(name[i]) || choice.equals(number[i])) {

					// Store the found contact in the array
					foundContacts[foundContactsCount++] = name[i] + "\t" + number[i];
				}
			}

			// Display the search results
			if (foundContactsCount == 0) {
				System.out.println("No contact found. Try entering the whole name or number.");

			} else {

				System.out.println("Here are the contacts found: ");
				for (int j = 0; j < foundContactsCount; j++) {
					System.out.println(foundContacts[j]);
				}
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/*
	 * Runs the whole program
	 * Starts program by displaying the menu and asking user what they want to do
	 */
	public static void main(String[] args) throws IOException {

		//Display menu to start program and give the user a choice
		displayMenu();
	}

}

