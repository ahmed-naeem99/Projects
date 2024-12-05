/*
 * Name: Ahmed Naeem
 * Date: March 20, 2024
 * Description: Contact Manager Graphical User Interface allowing users to add, delete, modify, sort, save, and load contacts.
 * 				Uses ArrayList to store the contacts in a 1D ArrayList 
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
public class ContactManagerV4 extends JFrame {

	private static final long serialVersionUID = -7166838048295809897L;

	private JTextField firstNameField, lastNameField, addressField, phoneNumberField, emailField;
	private DefaultTableModel tableModel;
	private boolean unsaved = false;
	private Font font = new Font("Montserrat", Font.PLAIN, 14);

	//
	private Contacts contactList = new Contacts();

	/*
	 * This constructor creates and initializes the two drop down menus, file and
	 * edit, and includes all the settings in there
	 */
	public ContactManagerV4() {

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
			JOptionPane.showMessageDialog(this, "Phone number must be 10 digits.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
			JOptionPane.showMessageDialog(this, "Invalid email address.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	// Method to show dialog for adding contacts
	private void showAddContactDialog() {

		// Clear fields first to ensure the dialog starts with empty fields.
		clearFields();

		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.add(new JLabel("First Name:"));
		panel.add(firstNameField);
		panel.add(new JLabel("Last Name:"));
		panel.add(lastNameField);
		panel.add(new JLabel("Address:"));
		panel.add(addressField);
		panel.add(new JLabel("Phone Number:"));
		panel.add(phoneNumberField);
		panel.add(new JLabel("Email:"));
		panel.add(emailField);

		int result = JOptionPane.showConfirmDialog(null, panel, "Add Contact", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			// Use the addContact method to add the contact to the ArrayList
			addContact(firstNameField.getText(), lastNameField.getText(), addressField.getText(),
					phoneNumberField.getText(), emailField.getText());
			// Optionally clear fields here if you want to reset after each successful
			// addition.
			clearFields();
		}
	}

	// Method to add contacts
	private void addContact(String firstName, String lastName, String address, String phoneNumber, String email) {
		if (validateInputs(firstName, lastName, phoneNumber, email)) {
			ContactEntry newEntry = new ContactEntry(firstName, lastName, address, phoneNumber, email);
			// contactEntries.add(newEntry);
			contactList.addContact(newEntry);
			tableModel.addRow(new String[] { firstName, lastName, address, phoneNumber, email });
			unsaved = true;
		}
	}

	// Method to delete a contact
	private void deleteEntry() {
		try {
			String choice = JOptionPane.showInputDialog("Enter first name or phone number to delete:");
			ContactEntry contactToDelete = contactList.searchContact(choice);

			if (contactToDelete != null) {
				String contactDetails = contactToDelete.getContactDetails();
				int confirm = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to delete this contact?\n\n" + contactDetails, "Delete Contact",
						JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					contactList.deleteContact(contactToDelete);
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
			ContactEntry contactToModify = contactList.searchContact(choice);

			if (contactToModify != null) {
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
					contactToModify.setFirstName(firstNameField.getText());
					contactToModify.setLastName(lastNameField.getText());
					contactToModify.setAddress(addressField.getText());
					contactToModify.setPhoneNumber(phoneNumberField.getText());
					contactToModify.setEmail(emailField.getText());

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
		contactList.sortContactsByName(sortOrder);
		unsaved = true;
		updateTableModel();
	}

	// Method to sort contacts by number
	private void sortContactsByNumber(int sortOrder) {
		contactList.sortContactsByNumber(sortOrder);
		unsaved = true;
		updateTableModel();
	}

	// Method to save contacts
	private void saveBook() {

		if (unsaved) {

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Specify a file to save");

			int userSelection = fileChooser.showSaveDialog(this);

			if (userSelection == JFileChooser.APPROVE_OPTION) {
				File fileToSave = fileChooser.getSelectedFile();
				try (PrintWriter writer = new PrintWriter(new FileWriter(fileToSave))) {

					contactList.writeAllContacts(writer);
					JOptionPane.showMessageDialog(null, "Contacts saved successfully!", "Save Contacts",
							JOptionPane.INFORMATION_MESSAGE);
					unsaved = false;

				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Error saving contacts: " + e.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "No unsaved changes.");
		}
	}

	// Method to load contacts
	private void loadBook() {

		// Confirm if user would like to load book with unsaved changes
		if (unsaved) {
			int confirm = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to continue? Your unsaved changes will not be saved.", "Load Book",
					JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.NO_OPTION) {
				return;
			}
		}
		
		// If there are no unsaved changes or the user would like to continue without saving changes, load the book
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify a file to load");

		int userSelection = fileChooser.showOpenDialog(this);

		contactList.clearContacts();
		updateTableModel();
		
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToLoad = fileChooser.getSelectedFile();
			try (BufferedReader reader = new BufferedReader(new FileReader(fileToLoad))) {
				String line;
				while ((line = reader.readLine()) != null) {
					String[] parts = line.split(",");
					String firstName = parts[0];
					String lastName = parts[1];
					String address = parts[2];
					String phoneNumber = parts[3];
					String email = parts[4];
					ContactEntry entry = new ContactEntry(firstName, lastName, address, phoneNumber, email);
					contactList.addContact(entry);
					tableModel.addRow(parts);
				}
				JOptionPane.showMessageDialog(null, "Contacts loaded successfully!", "Load Contacts",
						JOptionPane.INFORMATION_MESSAGE);
				unsaved = false;
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Error loading contacts: " + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	// Method to clear input fields
	private void clearFields() {
		firstNameField.setText("");
		lastNameField.setText("");
		addressField.setText("");
		phoneNumberField.setText("");
		emailField.setText("");
	}

	// Method to update table after modifications
	private void updateTableModel() {
		tableModel.setRowCount(0);
		String[][] contactArray = contactList.getContactsAsStringArray();
		for (int i = 0; i < contactArray.length; i++) {
			tableModel.addRow(contactArray[i]);
		}
	}

	// Main method to run the application
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ContactManagerV4();
			}
		});
	}
}
