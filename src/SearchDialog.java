
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class SearchDialog extends JDialog implements ActionListener {

	EmployeeDetails parent;
	JButton search, cancel;
	JTextField searchField;

	String dialog = "";
	String type = "";

	public SearchDialog(EmployeeDetails parent, String type) {
		this.type = type;
		setTitle("Search By: " + type);

		setModal(true);
		this.parent = parent;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// this.dialog = id;

		JScrollPane scrollPane = new JScrollPane(searchPane());
		setContentPane(scrollPane);
		getRootPane().setDefaultButton(search);
		setSize(500, 190);
		setLocation(350, 250);
		setVisible(true);

	}
	
	public void searchEmployeeBySurname(JTextField searchBySurnameField,JTextField surnameField) {
		boolean found = false;
	
		//this.parent.firstRecord();
		String firstSurname = this.parent.currentEmployee.getSurname().trim();
		if (searchBySurnameField.getText().trim().equalsIgnoreCase(surnameField.getText().trim()))
				found = true;
			else if (searchBySurnameField.getText().trim().equalsIgnoreCase(this.parent.currentEmployee.getSurname().trim())) {
				found = true;
				this.parent.displayEmployeeRecords(this.parent.currentEmployee);
			} // end else if
			else {
				this.parent.nextRecord();
				// loop until Employee found or until all Employees have been checked
				while (!firstSurname.trim().equalsIgnoreCase(this.parent.currentEmployee.getSurname().trim())) {
					if (searchBySurnameField.getText().trim().equalsIgnoreCase(this.parent.currentEmployee.getSurname().trim())) {
						found = true;
						this.parent.displayEmployeeRecords(this.parent.currentEmployee);
						break;
					}
					else
						this.parent.nextRecord();
				}
			}
			if (!found)
				JOptionPane.showMessageDialog(null, "Employee not found!");
		searchBySurnameField.setText("");
		} 
	
	public void searchEmployeeById(JTextField searchByIdField, JTextField idField) {
		boolean found = false;
		try {
			if (this.parent.isSomeoneToDisplay()) {
				this.parent.firstRecord();
				int firstId = this.parent.currentEmployee.getEmployeeId();
				if (searchByIdField.getText().trim().equals(idField.getText().trim()))
					found = true;
				else if (searchByIdField.getText().trim().equals(Integer.toString(this.parent.currentEmployee.getEmployeeId()))) {
					found = true;
					this.parent.displayEmployeeRecords(this.parent.currentEmployee);
				}
				else {
					this.parent.nextRecord();
					while (firstId != this.parent.currentEmployee.getEmployeeId()) {
							if (Integer.parseInt(searchByIdField.getText().trim()) == this.parent.currentEmployee.getEmployeeId()) {
							found = true;
							this.parent.displayEmployeeRecords(this.parent.currentEmployee);
							break;
						} else
							this.parent.nextRecord();
					}
				}
				if (!found)
					JOptionPane.showMessageDialog(null, "Employee not found!");
			} 
		} 
		catch (NumberFormatException e) {
			searchByIdField.setBackground(Colors.red);
			JOptionPane.showMessageDialog(null, "Wrong ID format!");
		}
		searchByIdField.setBackground(Colors.white);
		searchByIdField.setText("");
	}
	         
	public SearchDialog(EmployeeDetails parent) {
		super();
		this.parent = parent;
	}

	

	public Container searchPane() {
		JPanel searchPanel = new JPanel(new GridLayout(3, 1));
		JPanel textPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JLabel searchLabel;
		searchPanel.add(new JLabel(dialog));
		textPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		textPanel.add(searchLabel = new JLabel("Enter " + getDialog() + " :"));
		searchLabel.setFont(this.parent.font1);
		textPanel.add(searchField = new JTextField(20));
		searchField.setFont(this.parent.font1);
		searchField.setDocument(new JTextFieldLimit(20));

		buttonPanel.add(search = new JButton("Search"));
		search.addActionListener(this);
		search.requestFocus();

		buttonPanel.add(cancel = new JButton("Cancel"));
		cancel.addActionListener(this);
		searchPanel.add(textPanel);
		searchPanel.add(buttonPanel);
		return searchPanel;
	}
	public String getDialog() {
		return dialog;
	}

	public void setDialog(String dialog) {
		this.dialog = dialog;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == search && type.equals("ID")) {
			try {
				Double.parseDouble(searchField.getText());
				this.parent.searchByIdField.setText(searchField.getText());

				this.parent.searchEmployeeById();
				dispose();
			} catch (NumberFormatException num) {
				searchField.setBackground(Colors.red);
				JOptionPane.showMessageDialog(null, "Wrong ID format!");
			} 
		}
		else if (e.getSource() == search && type.equals("Surname")) {
			this.parent.searchBySurnameField.setText(searchField.getText());
			this.parent.searchEmployeeBySurname();
			dispose();// dispose dialog
		}

		else if (e.getSource() == cancel)
			dispose();
	}

}