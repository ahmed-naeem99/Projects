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
 * 	quit
 * Input: Name/number to input, name/number to search to delete/modify/read
 * Output: ContactBook.txt, 
 * 
 * errors to fix: ask user which file to load, which file to save, if user loads a file, save onto same file
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

public class Contact {

	//Initializing buffered reader for user input
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	//String arrays containing contact information, and main contact book which adds both arrays together
	static String[] name = new String[100];
	static String[] number = new String[100];
	static String[] book = new String[100];

	//Initialize counter to control array limit
	static int count = 0;	

	/*
	 * Method that displays the main menu with all the options listed in the description of the program 
	 */
	private static void displayMenu() throws IOException {

		System.out.println("\nMain Menu >> \n1. Add a contact entry \n2. View all contacts in book \n3. Modify a contact entry \n4. Delete a contact entry \n5. Save the contact book \n6. Load the contact book \n7. Quit \n\nEnter your choice: ");

		//Read the input from the main menu and convert it into an integer for the switch method
		int choice = Integer.parseInt(in.readLine());

		/*
		 * Switch method that executes the program based on the choice the user gave from the main menu
		 * Option 1: add an entry
		 * Option 2: view all the entries
		 * Option 3: modify an entry
		 * Option 4: delete an entry
		 * Option 5: save the contact book
		 * Option 6: load the contact book
		 * Option 7: quit
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
			System.out.println("Quitting the program...Bye!");
			break;
		}
	}

	/*
	 * Method that adds name and number from user input into one contact in the contact book 
	 */
	private static void addToContactBook() throws IOException {

		for (int i = 0; i < book.length; i++) {
			book[i] = name[i] + "\t" + number[i];  
		}
	}

	/*
	 * Method that adds a contact to the Contact Book 
	 * Uses InputStreamReader to read name or number that user gives and adds it to the list
	 * Only adds the name or number if the count variable does not exceed the Contact Book array length
	 */
	private static void addEntry() throws IOException {

		try {
			//Checking if there is space in the contact book
			if (count < book.length) {

				//Ask user for input to add contact name to string array
				System.out.println("Enter the contact's name: ");
				name[count] = in.readLine();

				//Ask user for input to add contact number to string array			
				System.out.println("Enter the contact's number: ");
				number[count] = in.readLine();

				//Add counter by 1
				count++;

				System.out.println("Entry Added!");

				//Call function to add name and number to one input in the contact book
				addToContactBook();

			}

		} catch (IOException e) {

			//If contact book is full, tell user to delete a contact first
			System.out.println("The contact book is full. Try deleting an entry first!");
		}

		//Display main menu again after adding the name
		displayMenu();
	}

	/*
	 * Method that displays all the contacts that are in the Contact Book
	 * Uses a for loop and loops through every index of the string array and outputs it
	 */
	private static void viewEntries() throws IOException {

		for (int i = 0; i < count; i++) {
			System.out.println(book[i]);
		}

		//Display main menu again after displaying the names
		displayMenu();
	}
	/*
	 * Method that modifies a contact thats in the Contact Book
	 * First searches the Contact Book by name or number
	 * Then gets user input for the new name
	 */
	private static void modifyEntry() throws IOException {

		try {
			//Set flag and boolean found to 0 and false because contact is not found yet
			int flag = -1;
			boolean found = false;

			System.out.println("Which entry do you want to modify? Enter name or number: ");

			//Read user input
			String choice = in.readLine();

			for(int i = 0; i < book.length; i++) {

				//Look for a match in the name or number array, if found, set flag to index number, and found to true
				if (choice.equals(name[i]) || choice.equals(number[i])) {
					flag = i;
					found = true;
					break;
				}
			}

			//When contact is found, get user input to modify contact
			if (found == true) {

				System.out.println("\nEnter the new name: ");
				name[flag] = in.readLine();

				System.out.println("\nEnter the new number: ");
				number[flag] = in.readLine();

				//Call function to add name and number to one input in the contact book
				addToContactBook();

				System.out.println("Entry has been modified");

			} else {

				System.out.println("Name/Number not found");
			}

		} catch (IOException e) {

			System.out.println(e.getMessage());
		}

		//Display main menu again after modifying the names
		displayMenu();
	}

	/*
	 * Method that deletes a contact thats in the Contact Book
	 * First searches the Contact Book by name or number
	 * Then deletes the contact and shifts all the indexes under that contact up by one
	 */
	private static void deleteEntry() throws IOException {

		try {
			//Initialize flag to 0, in case user wants to delete first index
			int flag = -1;

			System.out.println("Which entry do you want to delete? Enter name or number: ");
			String choice = in.readLine();

			for(int i = 0; i < book.length; i++) {

				//Look for contact in name or number array, if found, set flag to index number
				if (choice.equals(name[i]) || choice.equals(number[i])) {
					flag = i;
					break;
				} 
			}

			if (flag == -1) {
				System.out.println("Name/Number not found");

				//Shift all indexes under the deleted contact by 1 and then delete the contact	
			} else {
				for (int i = flag; i < count - 1; i++) {
					name[i] = name[i + 1];
					number[i] = number[i + 1];
					book[i] = book[i + 1];
				}

				name[count - 1] = null;
				number[count - 1] = null;
				book[count - 1] = null;

				count--; 

				System.out.println("Entry has been deleted");
			}

		} catch (IOException e) {

			System.out.println(e.getMessage());
		}
		//Display menu after deleting the names
		displayMenu();
	}
	/*
	 * Method that saves all the contacts into a file 
	 * Takes all added contacts in the book string array in case user wants to load file next program
	 */
	private static void saveBook() throws IOException {

		//Try/catch block to catch errors
		try {

			//Ask user for file name to save contacts in
			System.out.println("Which file do you want to save the contacts in?");

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

			//Close file
			output.close();

			System.out.println("File saved!");

		} catch (IOException e) {

			System.out.println("An error occurred while saving the file: " + e.getMessage());

		} 

		//Display menu after saving the file
		displayMenu();
	}

	/*
	 * Method that loads the Contact Book 
	 * If Contact Book file does not exist, the catch statement creates one
	 */
	private static void loadBook() throws IOException {

		System.out.println("Which file do you want to load?");

		//Read user input
		String loadFile = in.readLine();

		count = 0;


		try (BufferedReader reader = new BufferedReader(new FileReader(loadFile))) {

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
		}

		//Display menu after loading the file
		displayMenu();
	}


	public static void main(String[] args) throws IOException {

		//Display menu to start program and give the user a choice
		displayMenu();
	}

}
