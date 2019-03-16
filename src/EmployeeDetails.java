
/* * 
 * This is a menu driven system that will allow users to define a data structure representing a collection of 
 * records that can be displayed both by means of a dialog that can be scrolled through and by means of a table
 * to give an overall view of the collection contents.
 * 
 * */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

public class EmployeeDetails extends JFrame implements ActionListener, ItemListener, DocumentListener, WindowListener {
	// decimal format for inactive currency text field
	private static final DecimalFormat format = new DecimalFormat("\u20ac ###,###,##0.00");
	// decimal format for active currency text field
	private static final DecimalFormat fieldFormat = new DecimalFormat("0.00");
	// hold object start position in file
	private long currentEmployeePosition = 0;
	private FileManager fileManager = new FileManager();
	// display files in File Chooser only with extension .dat
	private FileNameExtensionFilter datfilter = new FileNameExtensionFilter("dat files (*.dat)", "dat");
	// hold file name and path for current file in use
	private File file;
	// holds true or false if any changes are made for text fields
	private boolean change = false;
	// holds true or false if any changes are made for file content
	boolean changesMade = false;
	private JMenuItem open, save, saveAs, create, modify, delete, firstItem, lastItem, nextItem, prevItem, searchById,
			searchBySurname, listAll, closeApp;
	private JButton first, previous, next, last, add, edit, deleteButton, displayAll, searchId, searchSurname,
			saveChange, cancelChange;
	private JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
	private JTextField idField, ppsField, surnameField, firstNameField, salaryField;
	private static EmployeeDetails frame = new EmployeeDetails();
	Font font1 = new Font("SansSerif", Font.BOLD, 16);
	String generatedFileName;
	Validator vd = new Validator();
	Employee currentEmployee;
	JTextField searchByIdField, searchBySurnameField;
	String[] gender = { "", "M", "F" };
	String[] department = { "", "Administration", "Production", "Transport", "Management" };
	String[] fullTime = { "", "Yes", "No" };
	Colors colors;
	MigLayoutManager mlm;

	// initialize menu bar
	private JMenuBar menuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu, recordMenu, navigateMenu, closeMenu;

		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		recordMenu = new JMenu("Records");
		recordMenu.setMnemonic(KeyEvent.VK_R);
		navigateMenu = new JMenu("Navigate");
		navigateMenu.setMnemonic(KeyEvent.VK_N);
		closeMenu = new JMenu("Exit");
		closeMenu.setMnemonic(KeyEvent.VK_E);

		menuBar.add(fileMenu);
		menuBar.add(recordMenu);
		menuBar.add(navigateMenu);
		menuBar.add(closeMenu);

		fileMenu.add(open = new JMenuItem("Open")).addActionListener(this);
		open.setMnemonic(KeyEvent.VK_O);
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		fileMenu.add(save = new JMenuItem("Save")).addActionListener(this);
		save.setMnemonic(KeyEvent.VK_S);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		fileMenu.add(saveAs = new JMenuItem("Save As")).addActionListener(this);
		saveAs.setMnemonic(KeyEvent.VK_F2);
		saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, ActionEvent.CTRL_MASK));

		recordMenu.add(create = new JMenuItem("Create new Record")).addActionListener(this);
		create.setMnemonic(KeyEvent.VK_N);
		create.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		recordMenu.add(modify = new JMenuItem("Modify Record")).addActionListener(this);
		modify.setMnemonic(KeyEvent.VK_E);
		modify.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		recordMenu.add(delete = new JMenuItem("Delete Record")).addActionListener(this);

		navigateMenu.add(firstItem = new JMenuItem("First"));
		firstItem.addActionListener(this);
		navigateMenu.add(prevItem = new JMenuItem("Previous"));
		prevItem.addActionListener(this);
		navigateMenu.add(nextItem = new JMenuItem("Next"));
		nextItem.addActionListener(this);
		navigateMenu.add(lastItem = new JMenuItem("Last"));
		lastItem.addActionListener(this);
		navigateMenu.addSeparator();
		navigateMenu.add(searchById = new JMenuItem("Search by ID")).addActionListener(this);
		navigateMenu.add(searchBySurname = new JMenuItem("Search by Surname")).addActionListener(this);
		navigateMenu.add(listAll = new JMenuItem("List all Records")).addActionListener(this);

		closeMenu.add(closeApp = new JMenuItem("Close")).addActionListener(this);
		closeApp.setMnemonic(KeyEvent.VK_F4);
		closeApp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.CTRL_MASK));

		return menuBar;
	}// end menuBar

	// initialize search panel
	private JPanel searchPanel() {
		JPanel searchPanel = new JPanel(new MigLayout());

		searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
		searchPanel.add(new JLabel("Search by ID:"), mlm.mig1);
		searchPanel.add(searchByIdField = new JTextField(20), "width 200:200:200, growx, pushx");
		searchByIdField.addActionListener(this);
		searchByIdField.setDocument(new JTextFieldLimit(20));
		searchPanel.add(searchId = new JButton(new ImageIcon(
				new ImageIcon("imgres.png").getImage().getScaledInstance(35, 20, java.awt.Image.SCALE_SMOOTH))),
				"width 35:35:35, height 20:20:20, growx, pushx, wrap");
		searchId.addActionListener(this);
		searchId.setToolTipText("Search Employee By ID");

		searchPanel.add(new JLabel("Search by Surname:"), mlm.mig1);
		searchPanel.add(searchBySurnameField = new JTextField(20), "width 200:200:200, growx, pushx");
		searchBySurnameField.addActionListener(this);
		searchBySurnameField.setDocument(new JTextFieldLimit(20));
		searchPanel.add(
				searchSurname = new JButton(new ImageIcon(new ImageIcon("imgres.png").getImage()
						.getScaledInstance(35, 20, java.awt.Image.SCALE_SMOOTH))),
				"width 35:35:35, height 20:20:20, growx, pushx, wrap");
		searchSurname.addActionListener(this);
		searchSurname.setToolTipText("Search Employee By Surname");

		return searchPanel;
	}// end searchPanel

	// initialize navigation panel
	private JPanel navigPanel() {
		JPanel navigPanel = new JPanel();

		navigPanel.setBorder(BorderFactory.createTitledBorder("Navigate"));
		navigPanel.add(first = new JButton(new ImageIcon(
				new ImageIcon("first.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		first.setPreferredSize(new Dimension(17, 17));
		first.addActionListener(this);
		first.setToolTipText("Display first Record");

		navigPanel.add(previous = new JButton(new ImageIcon(new ImageIcon("prev.png").getImage()
				.getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		previous.setPreferredSize(new Dimension(17, 17));
		previous.addActionListener(this);
		previous.setToolTipText("Display next Record");

		navigPanel.add(next = new JButton(new ImageIcon(
				new ImageIcon("next.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		next.setPreferredSize(new Dimension(17, 17));
		next.addActionListener(this);
		next.setToolTipText("Display previous Record");

		navigPanel.add(last = new JButton(new ImageIcon(
				new ImageIcon("last.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		last.setPreferredSize(new Dimension(17, 17));
		last.addActionListener(this);
		last.setToolTipText("Display last Record");

		return navigPanel;
	}// end naviPanel

	private JPanel buttonPanel() {
		JPanel buttonPanel = new JPanel();

		buttonPanel.add(add = new JButton("Add Record"), mlm.mig1);
		add.addActionListener(this);
		add.setToolTipText("Add new Employee Record");
		buttonPanel.add(edit = new JButton("Edit Record"), mlm.mig1);
		edit.addActionListener(this);
		edit.setToolTipText("Edit current Employee");
		buttonPanel.add(deleteButton = new JButton("Delete Record"), mlm.mig2);
		deleteButton.addActionListener(this);
		deleteButton.setToolTipText("Delete current Employee");
		buttonPanel.add(displayAll = new JButton("List all Records"), mlm.mig1);
		displayAll.addActionListener(this);
		displayAll.setToolTipText("List all Registered Employees");

		return buttonPanel;
	}

	// initialize main/details panel
	private JPanel detailsPanel() {
		JPanel empDetails = new JPanel(new MigLayout());
		JPanel buttonPanel = new JPanel();
		JTextField field;

		empDetails.setBorder(BorderFactory.createTitledBorder("Employee Details"));

		empDetails.add(new JLabel("ID:"), mlm.mig1);
		empDetails.add(idField = new JTextField(20), mlm.mig2);
		idField.setEditable(false);

		empDetails.add(new JLabel("PPS Number:"), mlm.mig1);
		empDetails.add(ppsField = new JTextField(20), mlm.mig2);

		empDetails.add(new JLabel("Surname:"), mlm.mig1);
		empDetails.add(surnameField = new JTextField(20), mlm.mig2);

		empDetails.add(new JLabel("First Name:"), mlm.mig1);
		empDetails.add(firstNameField = new JTextField(20), mlm.mig2);

		empDetails.add(new JLabel("Gender:"), mlm.mig1);
		empDetails.add(genderCombo = new JComboBox<String>(gender), mlm.mig2);

		empDetails.add(new JLabel("Department:"), mlm.mig1);
		empDetails.add(departmentCombo = new JComboBox<String>(department), mlm.mig2);

		empDetails.add(new JLabel("Salary:"), mlm.mig1);
		empDetails.add(salaryField = new JTextField(20), mlm.mig2);

		empDetails.add(new JLabel("Full Time:"), mlm.mig1);
		empDetails.add(fullTimeCombo = new JComboBox<String>(fullTime), mlm.mig2);

		buttonPanel.add(saveChange = new JButton("Save"));
		saveChange.addActionListener(this);
		saveChange.setVisible(false);
		saveChange.setToolTipText("Save changes");
		buttonPanel.add(cancelChange = new JButton("Cancel"));
		cancelChange.addActionListener(this);
		cancelChange.setVisible(false);
		cancelChange.setToolTipText("Cancel edit");

		empDetails.add(buttonPanel, mlm.mig3);

		// loop through panel components and add listeners and format
		for (int i = 0; i < empDetails.getComponentCount(); i++) {
			empDetails.getComponent(i).setFont(font1);
			if (empDetails.getComponent(i) instanceof JTextField) {
				field = (JTextField) empDetails.getComponent(i);
				field.setEditable(false);
				if (field == ppsField)
					field.setDocument(new JTextFieldLimit(9));
				else
					field.setDocument(new JTextFieldLimit(20));
				field.getDocument().addDocumentListener(this);
			} // end if
			else if (empDetails.getComponent(i) instanceof JComboBox) {
				empDetails.getComponent(i).setBackground(colors.white);
				empDetails.getComponent(i).setEnabled(false);
				((JComboBox<String>) empDetails.getComponent(i)).addItemListener(this);
				((JComboBox<String>) empDetails.getComponent(i)).setRenderer(new DefaultListCellRenderer() {
					// set foregroung to combo boxes
					public void paint(Graphics g) {
						setForeground(colors.foreground);
						super.paint(g);
					}// end paint
				});
			} // end else if
		} // end for
		return empDetails;
	}// end detailsPanel

	// display current Employee details
	public void displayRecords(Employee thisEmployee) {
		int countGender = 0;
		int countDep = 0;
		boolean found = false;

		searchByIdField.setText("");
		searchBySurnameField.setText("");

		if(thisEmployee != null && thisEmployee.getEmployeeId() !=0) {
			// find corresponding gender combo box value to current employee
			while (!found && countGender < gender.length - 1) {
				if (Character.toString(thisEmployee.getGender()).equalsIgnoreCase(gender[countGender]))
					found = true;
				else
					countGender++;
			} // end while
			found = false;
			// find corresponding department combo box value to current employee
			while (!found && countDep < department.length - 1) {
				if (thisEmployee.getDepartment().trim().equalsIgnoreCase(department[countDep]))
					found = true;
				else
					countDep++;
			} // end while
			idField.setText(Integer.toString(thisEmployee.getEmployeeId()));
			ppsField.setText(thisEmployee.getPps().trim());
			surnameField.setText(thisEmployee.getSurname().trim());
			firstNameField.setText(thisEmployee.getFirstName());
			genderCombo.setSelectedIndex(countGender);
			departmentCombo.setSelectedIndex(countDep);
			salaryField.setText(format.format(thisEmployee.getSalary()));
			// set corresponding full time combo box value to current employee
			if (thisEmployee.getFullTime() == true)
				fullTimeCombo.setSelectedIndex(1);
			else
				fullTimeCombo.setSelectedIndex(2);
		}
		change = false;
	}

	// display Employee summary dialog
	private void displayEmployeeSummaryDialog() {

		if (isSomeoneToDisplay())
			new EmployeeSummaryDialog(getAllEmloyees());
	}

	// display search by ID dialog
	private void displaySearchByIdDialog() {
		if (isSomeoneToDisplay())
			new SearchDialog(EmployeeDetails.this,"ID");
	}

	// display search by surname dialog
	private void displaySearchBySurnameDialog() {
		if (isSomeoneToDisplay())
			new SearchDialog(EmployeeDetails.this, "Surname");
	}

	// find byte start in file for first active record
	void firstRecord() {
		
		if (isSomeoneToDisplay()) {
			fileManager.openReadFile(file.getAbsolutePath());
			currentEmployeePosition = fileManager.getFirst();
			currentEmployee = fileManager.readRecords(currentEmployeePosition);
			fileManager.closeReadFile();
			// if first record is inactive look for next record
			if (currentEmployee.getEmployeeId() == 0)
				nextRecord();// look for next record
		} 
	}

	// find byte start in file for previous active record
	private void previousRecord() {
		if (isSomeoneToDisplay()) {
			fileManager.openReadFile(file.getAbsolutePath());
			currentEmployeePosition = fileManager.getPrevious(currentEmployeePosition);
			currentEmployee = fileManager.readRecords(currentEmployeePosition);
			while (currentEmployee.getEmployeeId() == 0) {
				currentEmployeePosition = fileManager.getPrevious(currentEmployeePosition);
				currentEmployee = fileManager.readRecords(currentEmployeePosition);
			} // end while
			fileManager.closeReadFile();
		}
	}

	// find byte start in file for next active record
	void nextRecord() {
		if (isSomeoneToDisplay()) {
			
			fileManager.openReadFile(file.getAbsolutePath());
			currentEmployeePosition = fileManager.getNext(currentEmployeePosition);
			currentEmployee = fileManager.readRecords(currentEmployeePosition);
			while (currentEmployee.getEmployeeId() == 0) {
				currentEmployeePosition = fileManager.getNext(currentEmployeePosition);
				currentEmployee = fileManager.readRecords(currentEmployeePosition);
			} // end while
			fileManager.closeReadFile();
		} 
	}
// find byte start in file for last active record
	private void lastRecord() {
		if (isSomeoneToDisplay()) {
		fileManager.openReadFile(file.getAbsolutePath());
			currentEmployeePosition = fileManager.getLast();
			currentEmployee = fileManager.readRecords(currentEmployeePosition);
			fileManager.closeReadFile();
			if (currentEmployee.getEmployeeId() == 0)
				previousRecord();// look for previous record
		} 
	}
	// search Employee by ID
 public void searchEmployeeById() {
	 
		SearchDialog search = new SearchDialog(EmployeeDetails.this);
		if (isSomeoneToDisplay()) {
			firstRecord();
//			System.out.println("HELLO");
		search.searchEmployeeById( searchByIdField, idField);
		}
		else {
			JOptionPane.showMessageDialog(null, "No Employees registered!");
		}
//		boolean found = false;
//		try {// try to read correct correct from input
//			if (isSomeoneToDisplay()) {
//				firstRecord();
//				int firstId = currentEmployee.getEmployeeId();
//				if (searchByIdField.getText().trim().equals(idField.getText().trim()))
//					found = true;
//				else if (searchByIdField.getText().trim().equals(Integer.toString(currentEmployee.getEmployeeId()))) {
//					found = true;
//					displayRecords(currentEmployee);
//				}
//				else {
//					nextRecord();
//					while (firstId != currentEmployee.getEmployeeId()) {
//							if (Integer.parseInt(searchByIdField.getText().trim()) == currentEmployee.getEmployeeId()) {
//							found = true;
//							displayRecords(currentEmployee);
//							break;
//						} else
//							nextRecord();
//					}
//				}
//				if (!found)
//					JOptionPane.showMessageDialog(null, "Employee not found!");
//			} 
//		} 
//		catch (NumberFormatException e) {
//			searchByIdField.setBackground(colors.red);
//			JOptionPane.showMessageDialog(null, "Wrong ID format!");
//		} // end catch
//		searchByIdField.setBackground(colors.white);
//		searchByIdField.setText("");
 }
	// search Employee by surname
//	public void searchEmployeeBySurname() {
//		boolean found = false;
//		if (isSomeoneToDisplay()) {
//			firstRecord();
//			String firstSurname = currentEmployee.getSurname().trim();
//			if (searchBySurnameField.getText().trim().equalsIgnoreCase(surnameField.getText().trim()))
//				found = true;
//			else if (searchBySurnameField.getText().trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
//				found = true;
//				displayRecords(currentEmployee);
//			} // end else if
//			else {
//				nextRecord();// look for next record
//				// loop until Employee found or until all Employees have been checked
//				while (!firstSurname.trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
//					if (searchBySurnameField.getText().trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
//						found = true;
//						displayRecords(currentEmployee);
//						break;
//					} // end if
//					else
//						nextRecord();// look for next record
//				} // end while
//			} // end else
//			if (!found)
//				JOptionPane.showMessageDialog(null, "Employee not found!");
//		} // end if
//		searchBySurnameField.setText("");
//	}
	// get next free ID from Employees in the file
	public int getNextFreeId() {
		int nextFreeId = 0;
		if (file.length() == 0 || !isSomeoneToDisplay())
			nextFreeId++;
		else {
			lastRecord();
			// add 1 to last active records ID to get next ID
			nextFreeId = currentEmployee.getEmployeeId() + 1;
		}
		return nextFreeId;
	}// end getNextFreeId
	// get values from text fields and create Employee object
	private Employee getChangedDetails() {
		boolean fullTime = false;
		Employee theEmployee;
		if (((String) fullTimeCombo.getSelectedItem()).equalsIgnoreCase("Yes"))
			fullTime = true;
				theEmployee = new Employee(Integer.parseInt(idField.getText()), ppsField.getText().toUpperCase(),
				surnameField.getText().toUpperCase(), firstNameField.getText().toUpperCase(),
				genderCombo.getSelectedItem().toString().charAt(0), departmentCombo.getSelectedItem().toString(),
				Double.parseDouble(salaryField.getText()), fullTime);
			return theEmployee;
	}
	// add Employee object to fail
	public void addRecord(Employee newEmployee) {
	fileManager.openWriteFile(file.getAbsolutePath());
	currentEmployeePosition = fileManager.addRecords(newEmployee);
		fileManager.closeWriteFile();
	}
	// delete (make inactive - empty) record from file
	private void deleteRecord() {
		if (isSomeoneToDisplay()) {
			int returnVal = JOptionPane.showOptionDialog(frame, "Do you want to delete record?", "Delete",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
			if (returnVal == JOptionPane.YES_OPTION) {
				fileManager.openWriteFile(file.getAbsolutePath());
				fileManager.deleteRecords(currentEmployeePosition);
				fileManager.closeWriteFile();
				if (isSomeoneToDisplay()) {
					nextRecord();
					displayRecords(currentEmployee);
				} 
				else {
					change = false;
				}
			} 
		} 
	}
	
	public void searchEmployeeBySurname() {
		SearchDialog search = new SearchDialog(EmployeeDetails.this);
		if (isSomeoneToDisplay()) {
			firstRecord();
//			System.out.println("HELLO");
		search.searchEmployeeBySurname( searchBySurnameField, surnameField);
		}
		else {
			JOptionPane.showMessageDialog(null, "No Employees registered!");
		}
	}
// create vector of vectors with all Employee details
	private Vector<Object> getAllEmloyees() {
		Vector<Object> allEmployee = new Vector<Object>();
		Vector<Object> empDetails;
		long position = currentEmployeePosition;
		int firstId;
		firstRecord();
		firstId = currentEmployee.getEmployeeId();
		do {
			empDetails = new Vector<Object>();
			empDetails.addElement(new Integer(currentEmployee.getEmployeeId()));
			empDetails.addElement(currentEmployee.getPps());
			empDetails.addElement(currentEmployee.getSurname());
			empDetails.addElement(currentEmployee.getFirstName());
			empDetails.addElement(new Character(currentEmployee.getGender()));
			empDetails.addElement(currentEmployee.getDepartment());
			empDetails.addElement(new Double(currentEmployee.getSalary()));
			empDetails.addElement(new Boolean(currentEmployee.getFullTime()));

			allEmployee.addElement(empDetails);
			nextRecord();
		} while (firstId != currentEmployee.getEmployeeId());// end do - while
		currentEmployeePosition = position;
		return allEmployee;
	}
	// activate field for editing
	private void editDetails() {
		if (isSomeoneToDisplay()) {
			// remove euro sign from salary text field
			salaryField.setText(fieldFormat.format(currentEmployee.getSalary()));
			change = false;
			setEnabled(true);// enable text fields for editing
		} 
	}
// ignore changes and set text field unenabled
	private void cancelChange() {
		setEnabled(false);
		displayRecords(currentEmployee);
	}
// check if any of records in file is active - ID is not 0
	boolean isSomeoneToDisplay() {
		boolean someoneToDisplay = false;
		fileManager.openReadFile(file.getAbsolutePath());
		someoneToDisplay = fileManager.isSomeoneToDisplay();
		fileManager.closeReadFile();
		if (!someoneToDisplay) {
			currentEmployee = null;
			idField.setText("");
			ppsField.setText("");
			surnameField.setText("");
			firstNameField.setText("");
			salaryField.setText("");
			genderCombo.setSelectedIndex(0);
			departmentCombo.setSelectedIndex(0);
			fullTimeCombo.setSelectedIndex(0);
			JOptionPane.showMessageDialog(null, "No Employees registered!");
		}
		return someoneToDisplay;
	}
	// check for correct PPS format and look if PPS already in use
	public boolean correctPps(String pps, long currentByte) {
		boolean ppsExist = false;
		if (pps.length() == 7 ) {
			if (Character.isDigit(pps.charAt(0)) && Character.isDigit(pps.charAt(1))
					&& Character.isDigit(pps.charAt(2))	&& Character.isDigit(pps.charAt(3)) 
					&& Character.isDigit(pps.charAt(4))	&& Character.isDigit(pps.charAt(5)) 
					&& Character.isLetter(pps.charAt(6))	
					) {
			fileManager.openReadFile(file.getAbsolutePath());
				ppsExist = fileManager.isPpsExist(pps, currentByte);
				fileManager.closeReadFile();// close file for reading
			} 
			else
				ppsExist = true;
		} // end if
		else
			ppsExist = true;
		return ppsExist;
	}
	// check if file name has extension .dat
	private boolean checkFileName(File fileName) {
		boolean checkFile = false;
		String file = fileName.toString();
		if(file.endsWith(".dat"))
			checkFile  = true;
		return checkFile;
	}
	// check if any changes text field where made
	private boolean checkForChanges() {
		boolean anyChanges = false;
		if (change) {
			saveChanges();// save changes
			anyChanges = true;
		}
		else {
			setEnabled(false);
			displayRecords(currentEmployee);
		}
		return anyChanges;
	}
	// check for input in text fields
	private boolean checkInput() {
		boolean valid2 = true;		
		valid2 = vd.validate2(ppsField,surnameField,firstNameField,genderCombo,departmentCombo,salaryField,fullTimeCombo);
		return valid2;
	}
	public void setEnabled(boolean booleanValue) {
		boolean search;
		if (booleanValue)
			search = false;
		else
			search = true;
		ppsField.setEditable(booleanValue);
		surnameField.setEditable(booleanValue);
		firstNameField.setEditable(booleanValue);
		genderCombo.setEnabled(booleanValue);
		departmentCombo.setEnabled(booleanValue);
		salaryField.setEditable(booleanValue);
		fullTimeCombo.setEnabled(booleanValue);
		saveChange.setVisible(booleanValue);
		cancelChange.setVisible(booleanValue);
		searchByIdField.setEnabled(search);
		searchBySurnameField.setEnabled(search);
		searchId.setEnabled(search);
		searchSurname.setEnabled(search);
	}
	private void openFile() {
		final JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Open");
		// display files in File Chooser only with extension .dat
		fc.setFileFilter(datfilter);
		File newFile; // holds opened file name and path
		// if old file is not empty or changes has been made, offer user to save old file
		if (file.length() != 0 || change) {
			int returnVal = JOptionPane.showOptionDialog(frame, "Do you want to save changes?", "Save",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
			if (returnVal == JOptionPane.YES_OPTION) {
				saveFile();
			} 
		} 
	int returnVal = fc.showOpenDialog(EmployeeDetails.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			newFile = fc.getSelectedFile();
			// if old file wasn't saved and its name is generated file name, delete this file
			if (file.getName().equals(generatedFileName))
				file.delete();
			file = newFile;		
			fileManager.openReadFile(file.getAbsolutePath());
			firstRecord();
			displayRecords(currentEmployee);
			fileManager.closeReadFile();// 
		} 
	}
	private void saveFile() {
		// if file name is generated file name, save file as 'save as' else save,changes to file
		if (file.getName().equals(generatedFileName))
			saveFileAs();
		else {
			// if changes has been made to text field offer user to save these changes
			if (change) {
				int returnVal = JOptionPane.showOptionDialog(frame, "Do you want to save changes?", "Save",
						JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
				if (returnVal == JOptionPane.YES_OPTION) {
					if (!idField.getText().equals("")) {
						fileManager.openWriteFile(file.getAbsolutePath());
						currentEmployee = getChangedDetails();
						fileManager.changeRecords(currentEmployee, currentEmployeePosition);
						fileManager.closeWriteFile();
					} 
				} 				
			} 
			displayRecords(currentEmployee);
			setEnabled(false);
		} // end else
	}
	// save changes to current Employee
	private void saveChanges() {
		int returnVal = JOptionPane.showOptionDialog(frame, "Do you want to save changes to current Employee?", "Save",
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
		if (returnVal == JOptionPane.YES_OPTION) {
			fileManager.openWriteFile(file.getAbsolutePath());
			currentEmployee = getChangedDetails();
			// write changes to file for corresponding Employee record
			fileManager.changeRecords(currentEmployee, currentEmployeePosition);
			fileManager.closeWriteFile();// close file for writing
			changesMade = false;// state that all changes has bee saved
		} // end if
		displayRecords(currentEmployee);
		setEnabled(false);
	}
	// save file as 'save as'
	private void saveFileAs() {
		final JFileChooser fc = new JFileChooser();
		File newFile;
		String defaultFileName = "new_Employee.dat";
		fc.setDialogTitle("Save As");
		fc.setFileFilter(datfilter);
		fc.setApproveButtonText("Save");
		fc.setSelectedFile(new File(defaultFileName));
		int returnVal = fc.showSaveDialog(EmployeeDetails.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			newFile = fc.getSelectedFile();
			if (!checkFileName(newFile)) {
				newFile = new File(newFile.getAbsolutePath() + ".dat");
				fileManager.createFile(newFile.getAbsolutePath());
			}
			else
				fileManager.createFile(newFile.getAbsolutePath());
			try {// try to copy old file to new file
				Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				if (file.getName().equals(generatedFileName))
					file.delete();
				file = newFile;
			} // end try
			catch (IOException e) {
			} 
		}
		changesMade = false;
	}
	// allow to save changes to file when exiting the fileManager
	private void exitApp() {
		// if file is not empty allow to save changes
		if (file.length() != 0) {
			if (changesMade) {
				int returnVal = JOptionPane.showOptionDialog(frame, "Do you want to save changes?", "Save",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
				if (returnVal == JOptionPane.YES_OPTION) {
					saveFile();
					if (file.getName().equals(generatedFileName))
						file.delete();
					System.exit(0);
				} 
				else if (returnVal == JOptionPane.NO_OPTION) {
					if (file.getName().equals(generatedFileName))
						file.delete();
					System.exit(0);
				} // end else if
			} // end if
			else {
				if (file.getName().equals(generatedFileName))
					file.delete();
				System.exit(0);
			} 
		} else {
			if (file.getName().equals(generatedFileName))
				file.delete();
			System.exit(0);
		}
	}
	// generate 20 character long file name
	private String getFileName() {
		String fileNameChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-";
		StringBuilder fileName = new StringBuilder();
		Random rnd = new Random();
		// loop until 20 character long file name is generated
		while (fileName.length() < 20) {
			int index = (int) (rnd.nextFloat() * fileNameChars.length());
			fileName.append(fileNameChars.charAt(index));
		}
		String generatedfileName = fileName.toString();
		return generatedfileName;
	}
	// create file with generated file name when fileManager is opened
	private void createRandomFile() {
		generatedFileName = getFileName() + ".dat";
		file = new File(generatedFileName);
		fileManager.createFile(file.getName());
	}
	// action listener for buttons, text field and menu items
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == searchId || e.getSource() == searchByIdField)
			searchEmployeeById();
		else if (e.getSource() == searchSurname || e.getSource() == searchBySurnameField)
			searchEmployeeBySurname();
		else if (e.getSource() == cancelChange)
			cancelChange();
		else if (checkInput() && !checkForChanges()) {
			if (e.getSource() == closeApp) {
				exitApp();
			}
			else if (e.getSource() == open) {
				openFile();
			}
			else if (e.getSource() == save) {
				saveFile();
				change = false;
			}
			else if (e.getSource() == saveAs) {
				saveFileAs();
				change = false;
			}
			else if (e.getSource() == searchById) {
				displaySearchByIdDialog();
			}
			else if (e.getSource() == searchBySurname) {
				displaySearchBySurnameDialog();
			}
			else if (e.getSource() == saveChange) {
				
			}
			else if (e.getSource() == firstItem || e.getSource() == first) {
				firstRecord();
				displayRecords(currentEmployee);
			}
			else if (e.getSource() == prevItem || e.getSource() == previous) {
				previousRecord();
				displayRecords(currentEmployee);
			}
			else if (e.getSource() == nextItem || e.getSource() == next) {
				nextRecord();
				displayRecords(currentEmployee);
			}
			else if (e.getSource() == lastItem || e.getSource() == last) {
				lastRecord();
				displayRecords(currentEmployee);
			}
			else if (e.getSource() == listAll || e.getSource() == displayAll) {
				if (isSomeoneToDisplay())
					displayEmployeeSummaryDialog();
			}
			else if (e.getSource() == create || e.getSource() == add) {
				new AddRecordDialog(EmployeeDetails.this);
			}
			else if (e.getSource() == modify || e.getSource() == edit) {
				editDetails();
			}
			else if (e.getSource() == delete || e.getSource() == deleteButton) {
				deleteRecord();
			}
			else if (e.getSource() == searchBySurname) {
				new SearchDialog(EmployeeDetails.this,"Surname");
			}
		}
		
	}
	// content pane for main dialog
	private void createContentPane() {
		setTitle("Employee Details");
		createRandomFile();// create random file name
		JPanel dialog = new JPanel(new MigLayout());
		setJMenuBar(menuBar());// add menu bar to frame
		dialog.add(searchPanel(), "width 400:400:400, growx, pushx");
		dialog.add(navigPanel(), "width 150:150:150, wrap");
		dialog.add(buttonPanel(), "growx, pushx, span 2,wrap");
		dialog.add(detailsPanel(), "gap top 30, gap left 150, center");
		JScrollPane scrollPane = new JScrollPane(dialog);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		addWindowListener(this);
	}
	// create and show main dialog
	private static void createAndShowGUI() {
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.createContentPane();// add content pane to frame
		frame.setSize(760, 600);
		frame.setLocation(250, 200);
		frame.setVisible(true);
	}// end createAndShowGUI
	// main method
	public static void main(String args[]) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
	// DocumentListener methods
	public void changedUpdate(DocumentEvent d) {
		change = true;
		new JTextFieldLimit(20);
	}
	public void insertUpdate(DocumentEvent d) {
		change = true;
		new JTextFieldLimit(20);
	}
	public void removeUpdate(DocumentEvent d) {
		change = true;
		new JTextFieldLimit(20);
	}
	// ItemListener method
	public void itemStateChanged(ItemEvent e) {
		change = true;
	}
	// WindowsListener methods
	public void windowClosing(WindowEvent e) {
		// exit fileManager
		exitApp();
	}
	public void windowActivated(WindowEvent e) {
	}
	public void windowClosed(WindowEvent e) {
	}
	public void windowDeactivated(WindowEvent e) {
	}
	public void windowDeiconified(WindowEvent e) {
	}
	public void windowIconified(WindowEvent e) {
	}
	public void windowOpened(WindowEvent e) {
	}
}// end class EmployeeDetails
