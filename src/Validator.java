import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class Validator {
	
	

	public Validator() {
		// TODO Auto-generated constructor stub
		

		
	}
	
	public boolean validate2(JTextField ppsField, JTextField surnameField, JTextField firstNameField, JComboBox<String> genderCombo, JComboBox<String> departmentCombo, JTextField salaryField, JComboBox<String> fullTimeCombo) {
		boolean valid = true;
		EmployeeDetails ed = new EmployeeDetails();
		if (ppsField.isEditable() && ppsField.getText().trim().isEmpty()) {
			ppsField.setBackground(Colors.red);
			valid = false;
		
		}
//		else {
//			ppsField.setEditable(false);
//		}
		///*if (ppsField.isEditable() /*&& ed.correctPps(ppsField.getText().trim(), currentByteStart)*/) {
		///	ppsField.setBackground(Colors.red);
			
		//	valid = false;
			
			//System.out.println("reached here" + valid);
		//} 
		if (surnameField.isEditable() && surnameField.getText().trim().isEmpty()) {
			surnameField.setBackground(Colors.red);
			valid = false;
		} 
		if (firstNameField.isEditable() && firstNameField.getText().trim().isEmpty()) {
			firstNameField.setBackground(Colors.red);
			valid = false;
		}
		if (genderCombo.getSelectedIndex() == 0 && genderCombo.isEnabled()) {
			genderCombo.setBackground(Colors.red);
			valid = false;
		} 
		if (departmentCombo.getSelectedIndex() == 0 && departmentCombo.isEnabled()) {
			departmentCombo.setBackground(Colors.red);
			valid = false;
		} 
		try {// try to get values from text field
			Double.parseDouble(salaryField.getText());
			// check if salary is greater than 0
			if (Double.parseDouble(salaryField.getText()) < 0) {
				salaryField.setBackground(Colors.red);
				valid = false;
			} 
		} 
		catch (NumberFormatException num) {
			if (salaryField.isEditable()) {
				salaryField.setBackground(Colors.red);
				valid = false;
			} // end if
		} // end catch
		if (fullTimeCombo.getSelectedIndex() == 0 && fullTimeCombo.isEnabled()) {
			fullTimeCombo.setBackground(Colors.red);
			valid = false;
		} 
		
		if (!valid)
			JOptionPane.showMessageDialog(null, "Wrong values or format! Please check!");
		
		// set text field to white colour if text fields are editable
		if (ppsField.isEditable()) {
			setToWhite(ppsField,surnameField,firstNameField,salaryField,genderCombo,departmentCombo,fullTimeCombo);
			
		}
		return valid;
	}
	
	private void setToWhite(JTextField ppsField, JTextField surnameField, JTextField firstNameField, JTextField salaryField, JComboBox<String> genderCombo, JComboBox<String> departmentCombo, JComboBox<String> fullTimeCombo) {
		ppsField.setBackground(UIManager.getColor("TextField.background"));
		surnameField.setBackground(UIManager.getColor("TextField.background"));
		firstNameField.setBackground(UIManager.getColor("TextField.background"));
		salaryField.setBackground(UIManager.getColor("TextField.background"));
		genderCombo.setBackground(UIManager.getColor("TextField.background"));
		departmentCombo.setBackground(UIManager.getColor("TextField.background"));
		fullTimeCombo.setBackground(UIManager.getColor("TextField.background"));
	}



	public boolean validate(String pps, String surname, String fName, int gender, int dept) {
		boolean valid = true;
		
	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		return valid;
		
		
	}
}
