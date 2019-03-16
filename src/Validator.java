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
		try {
			Double.parseDouble(salaryField.getText());

			if (Double.parseDouble(salaryField.getText()) < 0) {
				salaryField.setBackground(Colors.red);
				valid = false;
			} 
		} 
		catch (NumberFormatException num) {
			if (salaryField.isEditable()) {
				salaryField.setBackground(Colors.red);
				valid = false;
			} 
		} 
		if (fullTimeCombo.getSelectedIndex() == 0 && fullTimeCombo.isEnabled()) {
			fullTimeCombo.setBackground(Colors.red);
			valid = false;
		} 	
		if (!valid)
			JOptionPane.showMessageDialog(null, "Wrong values or format! Please check!");
		
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

}
