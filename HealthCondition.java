import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class HealthCondition extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField healthConditionIdField;
    private JTextField userIdField;
    private JTextField conditionNameField;
    private JTextField conditionDescriptionField;
    private JButton insertButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JTable displayTable;

    public HealthCondition() {
        setTitle("Health Condition System");
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        setVisible(true);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel healthConditionIdLabel = new JLabel("Health Condition ID:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(healthConditionIdLabel, constraints);

        healthConditionIdField = new JTextField(10);
        constraints.gridx = 1;
        constraints.gridy = 0;
        add(healthConditionIdField, constraints);

        JLabel userIdLabel = new JLabel("User ID:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(userIdLabel, constraints);

        userIdField = new JTextField(10);
        constraints.gridx = 1;
        constraints.gridy = 1;
        add(userIdField, constraints);

        JLabel conditionNameLabel = new JLabel("Condition Name:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(conditionNameLabel, constraints);

        conditionNameField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 2;
        add(conditionNameField, constraints);

        JLabel conditionDescriptionLabel = new JLabel("Condition Description:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        add(conditionDescriptionLabel, constraints);

        conditionDescriptionField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 3;
        add(conditionDescriptionField, constraints);

        insertButton = new JButton("Insert");
        constraints.gridx = 0;
        constraints.gridy = 4;
        add(insertButton, constraints);

        modifyButton = new JButton("Modify");
        constraints.gridx = 1;
        constraints.gridy = 4;
        add(modifyButton, constraints);

        deleteButton = new JButton("Delete");
        constraints.gridx = 2;
        constraints.gridy = 4;
        add(deleteButton, constraints);

        displayTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(displayTable);
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 3;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        add(scrollPane, constraints);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertHealthCondition();
            }
        });

        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyHealthCondition();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteHealthCondition();
            }
        });

        displayTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = displayTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int healthConditionId = Integer.parseInt(displayTable.getValueAt(selectedRow, 0).toString());
                    int userId = Integer.parseInt(displayTable.getValueAt(selectedRow, 1).toString());
                    String conditionName = displayTable.getValueAt(selectedRow, 2).toString();
                    String conditionDescription = displayTable.getValueAt(selectedRow, 3).toString();

                    healthConditionIdField.setText(String.valueOf(healthConditionId));
                    userIdField.setText(String.valueOf(userId));
                    conditionNameField.setText(conditionName);
                    conditionDescriptionField.setText(conditionDescription);
                }
            }
        });

        pack();
        setLocationRelativeTo(null);

        displayHealthConditions();
    }

    private void insertHealthCondition() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "kiran", "bunny")) {
            String sql = "INSERT INTO healthcondition (healthcondition_id, user_id, condition_name, condition_description) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(healthConditionIdField.getText()));
            statement.setInt(2, Integer.parseInt(userIdField.getText()));
            statement.setString(3, conditionNameField.getText());
            statement.setString(4, conditionDescriptionField.getText());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                displayHealthConditions();
                clearFields();
                JOptionPane.showMessageDialog(this, "Health condition inserted successfully.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to insert health condition: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifyHealthCondition() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "kiran", "bunny")) {
            String sql = "UPDATE healthcondition SET user_id = ?, condition_name = ?, condition_description = ? WHERE healthcondition_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(userIdField.getText()));
            statement.setString(2, conditionNameField.getText());
            statement.setString(3, conditionDescriptionField.getText());
            statement.setInt(4, Integer.parseInt(healthConditionIdField.getText()));

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                displayHealthConditions();
                clearFields();
                JOptionPane.showMessageDialog(this, "Health condition modified successfully.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to modify health condition: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteHealthCondition() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "kiran", "bunny")) {
            String sql = "DELETE FROM healthcondition WHERE healthcondition_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(healthConditionIdField.getText()));

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                displayHealthConditions();
                clearFields();
                JOptionPane.showMessageDialog(this, "Health condition deleted successfully.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete health condition: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayHealthConditions() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "kiran", "bunny")) {
            String sql = "SELECT healthcondition_id, user_id, condition_name, condition_description FROM healthcondition";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            displayTable.setModel(buildTableModel(resultSet));
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch health condition data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        healthConditionIdField.setText("");
        userIdField.setText("");
        conditionNameField.setText("");
        conditionDescriptionField.setText("");
    }

    private DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        // Get column names
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = metaData.getColumnLabel(i + 1);
        }

        // Get data rows
        Object[][] data = new Object[100][columnCount];
        int rowCount = 0;
        while (resultSet.next()) {
            for (int i = 0; i < columnCount; i++) {
                data[rowCount][i] = resultSet.getObject(i + 1);
            }
            rowCount++;
        }

        return new DefaultTableModel(data, columnNames);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new HealthCondition();
            }
        });
    }
}
