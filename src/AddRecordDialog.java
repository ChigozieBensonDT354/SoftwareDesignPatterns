/*
 * 
 * This is a dialog for adding new Employees and saving records to file
 * 
 * */

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class AddRecordDialog extends JDialog implements ActionListener {
	JTextField idField, ppsField, surnameField, firstNameField, salaryField;
	JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
	JButton save, cancel;
	EmployeeDetails parent;
	Color colors;
	MigLayoutManager mlm;
	// constructor for add record dialog
	public AddRecordDialog(EmployeeDetails parent) {
		setTitle("Add Record");
		setModal(true);
		this.parent = parent;
		this.parent.setEnabled(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JScrollPane scrollPane = new JScrollPane(dialogPane());
		setContentPane(scrollPane);
		
		getRootPane().setDefaultButton(save);
		
		setSize(500, 370);
		setLocation(350, 250);
		setVisible(true);
	}// end AddRecordDialog

	// initialize dialog container
	public Container dialogPane() {
		JPanel empDetails, buttonPanel;
		empDetails = new JPanel(new MigLayout());
		buttonPanel = new JPanel();
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
		empDetails.add(genderCombo = new JComboBox<String>(this.parent.gender), mlm.mig2);

		empDetails.add(new JLabel("Department:"), mlm.mig1);
		empDetails.add(departmentCombo = new JComboBox<String>(this.parent.department), mlm.mig2);

		empDetails.add(new JLabel("Salary:"), mlm.mig1);
		empDetails.add(salaryField = new JTextField(20), mlm.mig2);

		empDetails.add(new JLabel("Full Time:"), mlm.mig1);
		empDetails.add(fullTimeCombo = new JComboBox<String>(this.parent.fullTime), mlm.mig2);

		buttonPanel.add(save = new JButton("Save"));
		save.addActionListener(this);
		save.requestFocus();
		buttonPanel.add(cancel = new JButton("Cancel"));
		cancel.addActionListener(this);

		empDetails.add(buttonPanel, mlm.mig3);
		// loop through all panel components and add fonts and listeners
		for (int i = 0; i < empDetails.getComponentCount(); i++) {
			empDetails.getComponent(i).setFont(this.parent.font1);
			if (empDetails.getComponent(i) instanceof JComboBox) {
				empDetails.getComponent(i).setBackground(colors.white);
			}// end if
			else if(empDetails.getComponent(i) instanceof JTextField){
				field = (JTextField) empDetails.getComponent(i);
				if(field == ppsField)
					field.setDocument(new JTextFieldLimit(9));
				else
				field.setDocument(new JTextFieldLimit(20));
			}// end else if
		}// end for
		idField.setText(Integer.toString(this.parent.getNextFreeId()));
		return empDetails;
	}

	// add record to file
	public void addRecord() {
		boolean fullTime = false;
		Employee theEmployee;

		if (((String) fullTimeCombo.getSelectedItem()).equalsIgnoreCase("Yes"))
			fullTime = true;
		// create new Employee record with details from text fields
		theEmployee = new Employee(Integer.parseInt(idField.getText()), ppsField.getText().toUpperCase(), surnameField.getText().toUpperCase(),
				firstNameField.getText().toUpperCase(), genderCombo.getSelectedItem().toString().charAt(0),
				departmentCombo.getSelectedItem().toString(), Double.parseDouble(salaryField.getText()), fullTime);
		this.parent.currentEmployee = theEmployee;
		this.parent.addRecord(theEmployee);
		this.parent.displayRecords(theEmployee);
	}
	Validator vd = new Validator();
	// check for input in text fields
	public boolean checkInput() {
		boolean valid =vd.validate2(ppsField,surnameField,firstNameField,genderCombo,departmentCombo);
		// if any of inputs are in wrong format, colour text field and display message
		/*if (ppsField.getText().equals("")) {
			ppsField.setBackground(colors.red);
			valid = false;
		}// end if
		if (this.parent.correctPps(this.ppsField.getText().trim(), -1)) {
			ppsField.setBackground(colors.red);
			valid = false;
		}// end if
		if (surnameField.getText().isEmpty()) {
			surnameField.setBackground(colors.red);
			valid = false;
		}// end if
		if (firstNameField.getText().isEmpty()) {
			firstNameField.setBackground(colors.red);
			valid = false;
		}// end if
		if (genderCombo.getSelectedIndex() == 0) {
			genderCombo.setBackground(colors.red);
			valid = false;
		}// end if
		if (departmentCombo.getSelectedIndex() == 0) {
			departmentCombo.setBackground(colors.red);
			valid = false;
		}// end if
		*/
		if(valid == true) {
		try {// try to get values from text field
			Double.parseDouble(salaryField.getText());
			// check if salary is greater than 0
			if (Double.parseDouble(salaryField.getText()) < 0) {
				salaryField.setBackground(colors.red);
				valid = false;
			}// end if
		}// end try
		catch (NumberFormatException num) {
			salaryField.setBackground(colors.red);
			valid = false;
		}// end catch
		if (fullTimeCombo.getSelectedIndex() == 0) {
			fullTimeCombo.setBackground(colors.red);
			valid = false;
		}// end if
		}
		return valid;
	}// end checkInput

	// set text field to white colour
	public void setToWhite() {
		ppsField.setBackground(colors.white);
		surnameField.setBackground(colors.white);
		firstNameField.setBackground(colors.white);
		salaryField.setBackground(colors.white);
		genderCombo.setBackground(colors.white);
		departmentCombo.setBackground(colors.white);
		fullTimeCombo.setBackground(colors.white);
	}// end setToWhite

	// action performed
	public void actionPerformed(ActionEvent e) {
		// if chosen option save, save record to file
		if (e.getSource() == save) {
			// if inputs correct, save record
			if (checkInput()) {
				addRecord();// add record to file
				dispose();// dispose dialog
				this.parent.changesMade = true;
			}// end if
			// else display message and set text fields to white colour
			else {
				
				JOptionPane.showMessageDialog(null, "Wrong values or format! Please check!");
			setToWhite();
			}
		}// end if
		else if (e.getSource() == cancel)
			dispose();// dispose dialog
	}// end actionPerformed
}// end class AddRecordDialog