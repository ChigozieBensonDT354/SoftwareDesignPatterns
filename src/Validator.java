import javax.swing.JComboBox;
import javax.swing.JTextField;

public class Validator {
	
	

	public Validator() {
		// TODO Auto-generated constructor stub
		

		
	}
	
	public boolean validate2(JTextField ppsField, JTextField surnameField, JTextField firstNameField, JComboBox<String> genderCombo, JComboBox<String> departmentCombo) {
		boolean valid = true;
		EmployeeDetails ed = new EmployeeDetails();
		if (ppsField.isEditable() && ppsField.getText().trim().isEmpty()) {
			ppsField.setBackground(Colors.red);
			valid = false;
		} 
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
		return valid;
	}

	public boolean validate(String pps, String surname, String fName, int gender, int dept) {
		boolean valid = true;
		
	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		return valid;
		
		
	}
}
