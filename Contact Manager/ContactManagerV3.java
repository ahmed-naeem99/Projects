/*
 * Name: Ahmed Naeem
 * Date: March 20, 2024
 * Description: Contact Manager Graphical User Interface allowing users to add, delete, modify, sort, save, and load contacts.
 * 				Implements Java SWING and uses pop ups.
 * 
 * Input: User Selections, first name, last name, address, number, email, file name
 * Output: GUI displaying contacts under the respective field and pop ups for warning and input. Adds, deletes, modifies, sorts, saves, and loads contacts.
 */

package helloWorld;

// Import statements
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * This is the main class which contains the structure and implementation of the GUI
 * Extends JFrame for menu bar, text fields, pop ups, etc.
 * Includes serial version UID for version renewing
 */
public class ContactManagerV3 extends JFrame {

	private static final long serialVersionUID = -7166838048295809897L;

	private JTextField firstNameField, lastNameField, addressField, phoneNumberField, emailField;
	private DefaultTableModel tableModel;
	private int contactCount = 0;
	private boolean unsaved = false;
	private Font font = new Font("Montserrat", Font.PLAIN, 14); 
	private final int ASCENDING_ORDER = 1;
	private final int DESCENDING_ORDER = 2;
	
	//Create constructor to hold the contacts in a 1-D Array
	private ContactEntry[] contactEntries = new ContactEntry[100];

	/*
	 * This constructor creates and initializes the two drop down menus, file and
	 * edit, and includes all the settings in there
	 */
	public ContactManagerV3() {

		// Sets look of main GUI
		super("Contact Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(800, 600));
		getContentPane().setBackground(Color.WHITE);

		// Menu Bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// File Menu
		JMenu fileMenu = new JMenu("File"); // File menu
		fileMenu.setFont(font); // Set font for the menu
		fileMenu.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding
		menuBar.add(fileMenu);

		// Edit Menu
		JMenu editMenu = new JMenu("Edit"); // Edit menu
		editMenu.setFont(font); // Set font for the menu
		editMenu.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding
		menuBar.add(editMenu);

		// Add contact button
		JMenuItem addContactMenuItem = new JMenuItem("Add Contact"); // Add Contact menu item
		addContactMenuItem.setFont(font); // Set font for the menu item
		editMenu.add(addContactMenuItem);
		addContactMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAddContactDialog();
			}
		});

		// Delete contact button
		JMenuItem deleteContactMenuItem = new JMenuItem("Delete Contact"); // Delete Contact menu item
		deleteContactMenuItem.setFont(font); // Set font for the menu item
		editMenu.add(deleteContactMenuItem);
		deleteContactMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteEntry();
			}
		});

		// Modify contact button
		JMenuItem modifyContactMenuItem = new JMenuItem("Modify Contact"); // Modify Contact menu item
		modifyContactMenuItem.setFont(font); // Set font for the menu item
		editMenu.add(modifyContactMenuItem);
		modifyContactMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modifyEntry();
			}
		});

		// Sort contact button
		JMenuItem sortContactsMenuItem = new JMenuItem("Sort Contacts"); // Sort Contacts menu item
		sortContactsMenuItem.setFont(font); // Set font for the menu item
		editMenu.add(sortContactsMenuItem);
		sortContactsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sortContacts();
			}
		});

		// Save contacts button
		JMenuItem saveMenuItem = new JMenuItem("Save Contacts"); // Save Contacts menu item
		saveMenuItem.setFont(font); // Set font for the menu item
		fileMenu.add(saveMenuItem);
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveBook();
			}
		});

		// Load contacts button
		JMenuItem loadMenuItem = new JMenuItem("Load Contacts"); // Load Contacts menu item
		loadMenuItem.setFont(font); // Set font for the menu item
		fileMenu.add(loadMenuItem);
		loadMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadBook();
			}
		});

		fileMenu.addSeparator();

		// Quit GUI button
		JMenuItem quitMenuItem = new JMenuItem("Quit"); // Quit menu item
		quitMenuItem.setFont(font); // Set font for the menu item
		fileMenu.add(quitMenuItem);
		quitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (unsaved) {
					int confirm = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to continue? Your unsaved changes will not be saved.", "Quit",
							JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						System.exit(0); // Quit the application
					}
					// If user selects NO, do nothing
				} else {
					System.exit(0); // Quit the application directly without checking for unsaved changes
				}
			}
		});

		// Labels and Text Fields for Input
		firstNameField = new JTextField();
		firstNameField.setFont(font);
		lastNameField = new JTextField();
		lastNameField.setFont(font);
		addressField = new JTextField();
		addressField.setFont(font);
		phoneNumberField = new JTextField();
		phoneNumberField.setFont(font);
		emailField = new JTextField();
		emailField.setFont(font);

		// Table Panel
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());

		tableModel = new DefaultTableModel();
		tableModel.addColumn("First Name");
		tableModel.addColumn("Last Name");
		tableModel.addColumn("Address");
		tableModel.addColumn("Phone Number");
		tableModel.addColumn("Email");

		JTable table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);
		tablePanel.add(scrollPane, BorderLayout.CENTER);

		getContentPane().add(tablePanel, BorderLayout.CENTER);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/*
	 * Now implementations of actual methods begin, such as adding, deleting,
	 * modifying, sorting, saving, and loading contacts.
	 */

	
	// Method to validate input fields
	private boolean validateInputs(String firstName, String lastName, String phoneNumber, String email) {
		if (firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()) {
			JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (!firstName.matches("[a-zA-Z]+")) {
			JOptionPane.showMessageDialog(this, "First name cannot contain numbers.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (!lastName.matches("[a-zA-Z]+")) {
			JOptionPane.showMessageDialog(this, "Last name cannot contain numbers.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (!phoneNumber.matches("\\d+")) {
			JOptionPane.showMessageDialog(this, "Phone number must contain only digits.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		// Uses regular expression to check email validity
		if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
			JOptionPane.showMessageDialog(this, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	/*
	 * Add contact pop up that asks for input fields Includes submit and cancel
	 * button
	 */
	private void showAddContactDialog() {
		JDialog dialog = new JDialog(this, "Add Contact", true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setPreferredSize(new Dimension(400, 300));
		dialog.getContentPane().setBackground(Color.WHITE);

		Font font = new Font("Monsterrat", Font.PLAIN, 14);

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(5, 2, 5, 5));
		inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		inputPanel.setBackground(Color.WHITE);

		JLabel firstNameLabel = new JLabel("First Name:");
		firstNameLabel.setFont(font);
		JLabel lastNameLabel = new JLabel("Last Name:");
		lastNameLabel.setFont(font);
		JLabel addressLabel = new JLabel("Address:");
		addressLabel.setFont(font);
		JLabel phoneNumberLabel = new JLabel("Phone Number:");
		phoneNumberLabel.setFont(font);
		JLabel emailLabel = new JLabel("Email:");
		emailLabel.setFont(font);

		JTextField firstNameField = new JTextField();
		firstNameField.setFont(font);
		JTextField lastNameField = new JTextField();
		lastNameField.setFont(font);
		JTextField addressField = new JTextField();
		addressField.setFont(font);
		JTextField phoneNumberField = new JTextField();
		phoneNumberField.setFont(font);
		JTextField emailField = new JTextField();
		emailField.setFont(font);

		inputPanel.add(firstNameLabel);
		inputPanel.add(firstNameField);
		inputPanel.add(lastNameLabel);
		inputPanel.add(lastNameField);
		inputPanel.add(addressLabel);
		inputPanel.add(addressField);
		inputPanel.add(phoneNumberLabel);
		inputPanel.add(phoneNumberField);
		inputPanel.add(emailLabel);
		inputPanel.add(emailField);

		JButton submitButton = new JButton("Submit");
		submitButton.setFont(font);
		Dimension buttonSize = new Dimension(100, 50);
		submitButton.setPreferredSize(buttonSize);
		submitButton.setForeground(Color.BLACK);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(font);
		cancelButton.setPreferredSize(buttonSize);
		cancelButton.setForeground(Color.BLACK);

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose(); // Close the dialog
			}
		});

		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String firstName = firstNameField.getText();
				String lastName = lastNameField.getText();
				String address = addressField.getText();
				String phoneNumber = phoneNumberField.getText();
				String email = emailField.getText();

				if (validateInputs(firstName, lastName, phoneNumber, email)) {
					if (contactCount < contactEntries.length) {
						
						ContactEntry newContact = new ContactEntry(firstName, lastName, address, phoneNumber, email);
						
						contactEntries[contactCount] = newContact;

						contactCount++;

						updateTableModel();
						clearFields();

						JOptionPane.showMessageDialog(dialog, "Contact added successfully!");
						dialog.dispose();
					} else {
						JOptionPane.showMessageDialog(dialog, "Contact list is full.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.add(cancelButton);
		buttonPanel.add(submitButton);

		dialog.getContentPane().add(inputPanel, BorderLayout.CENTER);
		dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		dialog.pack();
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
		unsaved = true;
	}

	// Method to delete a contact
	private void deleteEntry() {
		try {
			String choice = JOptionPane.showInputDialog("Enter first name or phone number to delete:");
			int foundIndex = searchContactIndex(choice);

			if (foundIndex != -1) {

				String contactDetails = contactEntries[foundIndex].getContactDetails();

				// Ask for confirmation
				int confirm = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to delete this contact?\n\n" + contactDetails, "Delete Contact",
						JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					// Delete the contact
					// System.arraycopy(contacts, foundIndex + 1, contacts, foundIndex, contactCount
					// - foundIndex - 1);
					System.arraycopy(contactEntries, foundIndex + 1, contactEntries, foundIndex,
							contactCount - foundIndex - 1);
					contactCount--;

					// Update table model
					updateTableModel();

					JOptionPane.showMessageDialog(null, "Contact deleted successfully.");
				}
			} else {
				JOptionPane.showMessageDialog(null, "Contact not found.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			unsaved = true;

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "An error occurred while deleting the contact.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Method to modify a contact
	private void modifyEntry() {
		try {
			String choice = JOptionPane.showInputDialog("Enter first name or phone number to modify:");
			int foundIndex = searchContactIndex(choice);

			if (foundIndex != -1) {
				
				// Pre-fill fields in a dialog
				ContactEntry contactToModify = contactEntries[foundIndex];
				JTextField firstNameField = new JTextField(contactToModify.getFirstName());
				JTextField lastNameField = new JTextField(contactToModify.getLastName());
				JTextField addressField = new JTextField(contactToModify.getAddress());
				JTextField phoneNumberField = new JTextField(contactToModify.getPhoneNumber());
				JTextField emailField = new JTextField(contactToModify.getEmail());

				Object[] fields = { "First Name:", firstNameField, "Last Name:", lastNameField, "Address:",
						addressField, "Phone Number:", phoneNumberField, "Email:", emailField };

				int result = JOptionPane.showConfirmDialog(null, fields, "Edit Contact", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					// Update contact details
					contactEntries[foundIndex].setFirstName(firstNameField.getText());
					contactEntries[foundIndex].setLastName(lastNameField.getText());
					contactEntries[foundIndex].setAddress(addressField.getText());
					contactEntries[foundIndex].setPhoneNumber(phoneNumberField.getText());
					contactEntries[foundIndex].setEmail(emailField.getText());

					// Update table model
					updateTableModel();

					JOptionPane.showMessageDialog(null, "Contact modified successfully.");
				}
			} else {
				JOptionPane.showMessageDialog(null, "Contact not found.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			unsaved = true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "An error occurred while modifying the contact.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Method to sort contacts by either name or number, from least to greatest
	private void sortContacts() {
		try {
			int sortField = Integer.parseInt(JOptionPane.showInputDialog("Sort contacts by:\n1. Name\n2. Number"));

			switch (sortField) {
			case 1:
				int sortByName = Integer.parseInt(JOptionPane.showInputDialog("Sort by:\n1. A-Z\n2. Z-A"));
				sortContactsByName(sortByName);
				break;
			case 2:
				int sortByNumber = Integer.parseInt(JOptionPane.showInputDialog("Sort by:\n1. 0-9\n2. 9-0"));
				sortContactsByNumber(sortByNumber);
				break;
			default:
				JOptionPane.showMessageDialog(this, "Invalid input. Please enter 1 or 2.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Method to sort contacts by name
	private void sortContactsByName(int sortOrder) {
		for (int i = 0; i < contactCount - 1; i++) {
			for (int j = 0; j < contactCount - i - 1; j++) {
				if (contactEntries[j].nameGreaterThan(contactEntries[j + 1])) {
					if (sortOrder == ASCENDING_ORDER) {
						swap(j, j + 1);
					}
				} else {
					if (sortOrder == DESCENDING_ORDER) {
						swap(j, j + 1);
					}
				}
			}
		}

		unsaved = true;
		updateTableModel();
	}

	// Method to sort contacts by number
	private void sortContactsByNumber(int sortOrder) {
		for (int i = 0; i < contactCount - 1; i++) {
			for (int j = 0; j < contactCount - i - 1; j++) {
				if (contactEntries[j].numberGreaterThan(contactEntries[j + 1])) {
					if (sortOrder == ASCENDING_ORDER) {
						swap(j, j + 1);
					}
				} else {
					if (sortOrder == DESCENDING_ORDER) {
						swap(j, j + 1);
					}
				}
			}
		}

		unsaved = true;
		updateTableModel();
	}

	// Swap method for sorting, swaps contact's order
	private void swap(int i, int j) {
		ContactEntry temp = contactEntries[i];
		contactEntries[i] = contactEntries[j];
		contactEntries[j] = temp;
	}

	/* 
	 * Method to save contacts into a file. If new file is given, create and store contacts. 
	 * If existing file is given, overwrite contacts
	 */
	private void saveBook() {
		try {
			String fileName = JOptionPane.showInputDialog("Enter the file name to save contacts:");
			if (fileName == null || fileName.isEmpty()) {
				// User canceled or provided an empty file name
				return;
			}
			File file = new File(fileName);

			if (file.exists()) {
				// File already exists, ask user for action
				int option = JOptionPane.showOptionDialog(null, "File already exists. What would you like to do?",
						"File Exists", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
						new String[] { "Cancel", "Overwrite File" }, null);
				if (option == JOptionPane.CANCEL_OPTION) {
					// User chose to cancel
					return;
				} else if (option == JOptionPane.NO_OPTION) {
					// User chose to overwrite file
					try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
						saveContactsToFile(writer);
						JOptionPane.showMessageDialog(null, "Contacts overwritten successfully.");
						unsaved = false;
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "Error saving contacts.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				// File does not exist, proceed with regular save
				try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
					saveContactsToFile(writer);
					JOptionPane.showMessageDialog(null, "Contacts saved successfully.");
					unsaved = false;
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Error saving contacts.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "An error occurred while saving contacts.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Method to write contacts onto file
	private void saveContactsToFile(PrintWriter writer) {
		try {
			for (int i = 0; i < contactCount; i++) {
				writer.println(contactEntries[i].getContactRecord(","));
			}
		} finally {
			if (writer != null) {
				writer.close(); // Close the writer
			}
		}
	}

	// Method to open up a file with contacts stored in them
	private void loadBook() {
		try {
			String fileName = JOptionPane.showInputDialog("Enter the file name to load contacts:");
			File file = new File(fileName);

			if (file.exists()) {
				contactCount = 0;

				try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
					String line;
					while ((line = reader.readLine()) != null && contactCount < contactEntries.length) {
						String[] parts = line.split(",");
						if (parts.length == 5) {
							ContactEntry contactToLoad = new ContactEntry(parts[0], parts[1], parts[2], parts[3], parts[4]);
							contactEntries[contactCount] = contactToLoad;
							contactCount++;
						}
					}
				}
				unsaved = false;
				updateTableModel();

				JOptionPane.showMessageDialog(this, "Contacts loaded successfully.");
			} else {
				JOptionPane.showMessageDialog(this, "File not found.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "An error occurred while loading contacts.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Method to reset 
	private void clearFields() {
		firstNameField.setText("");
		lastNameField.setText("");
		addressField.setText("");
		phoneNumberField.setText("");
		emailField.setText("");
	}

	/*
	 * Method to search through the contact book to find the index of the contact the user is looking for.
	 * Used for deleting and modifying contacts 
	 */
	private int searchContactIndex(String key) {
		for (int i = 0; i < contactCount; i++) {
			if (contactEntries[i].getFirstName().equalsIgnoreCase(key)
					|| contactEntries[i].getPhoneNumber().equalsIgnoreCase(key)) {
				return i;
			}
		}
		return -1;
	}

	// Method to refresh the table when contact is deleted, modified, loaded, added, etc.
	private void updateTableModel() {
		// Clearing existing rows
		while (tableModel.getRowCount() > 0) {
			tableModel.removeRow(0);
		}

		// Adding new rows from the contact list
		for (int i = 0; i < contactCount; i++) {
			tableModel.addRow(new String[] { contactEntries[i].getFirstName(), contactEntries[i].getLastName(),
					contactEntries[i].getAddress(), contactEntries[i].getPhoneNumber(),
					contactEntries[i].getEmail() });
		}
	}

	/*
	 * Main function which opens the GUI
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(ContactManagerV3::new);
	}
}
