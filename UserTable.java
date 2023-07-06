import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class UserTable extends JFrame {
    private JTextField userIdField;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField heightField;
    private JTextField weightField;
    private JTextField activityLevelField;
    private JButton insertButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JTable displayTable;

    public UserTable() {
        setTitle("User Table");
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        setVisible(true);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel userIdLabel = new JLabel("User ID:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(userIdLabel, constraints);

        userIdField = new JTextField(10);
        constraints.gridx = 1;
        constraints.gridy = 0;
        add(userIdField, constraints);

        JLabel nameLabel = new JLabel("Name:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(nameLabel, constraints);

        nameField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        add(nameField, constraints);

        JLabel ageLabel = new JLabel("Age:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(ageLabel, constraints);

        ageField = new JTextField(10);
        constraints.gridx = 1;
        constraints.gridy = 2;
        add(ageField, constraints);

        JLabel heightLabel = new JLabel("Height:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        add(heightLabel, constraints);

        heightField = new JTextField(10);
        constraints.gridx = 1;
        constraints.gridy = 3;
        add(heightField, constraints);

        JLabel weightLabel = new JLabel("Weight:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        add(weightLabel, constraints);

        weightField = new JTextField(10);
        constraints.gridx = 1;
        constraints.gridy = 4;
        add(weightField, constraints);

        JLabel activityLevelLabel = new JLabel("Activity Level:");
        constraints.gridx = 0;
        constraints.gridy = 5;
        add(activityLevelLabel, constraints);

        activityLevelField = new JTextField(10);
        constraints.gridx = 1;
        constraints.gridy = 5;
        add(activityLevelField, constraints);

        insertButton = new JButton("Insert");
        constraints.gridx = 0;
        constraints.gridy = 6;
        add(insertButton, constraints);

        modifyButton = new JButton("Modify");
        constraints.gridx = 1;
        constraints.gridy = 6;
        add(modifyButton, constraints);

        deleteButton = new JButton("Delete");
        constraints.gridx = 2;
        constraints.gridy = 6;
        add(deleteButton, constraints);

        displayTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(displayTable);
        constraints.gridx = 0;
        constraints.gridy = 7;
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
                insertUser();
            }
        });

        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyUser();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });

        displayTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = displayTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int userId = Integer.parseInt(displayTable.getValueAt(selectedRow, 0).toString());
                    String name = displayTable.getValueAt(selectedRow, 1).toString();
                    int age = Integer.parseInt(displayTable.getValueAt(selectedRow, 2).toString());
                    int height = Integer.parseInt(displayTable.getValueAt(selectedRow, 3).toString());
                    int weight = Integer.parseInt(displayTable.getValueAt(selectedRow, 4).toString());
                    String activityLevel = displayTable.getValueAt(selectedRow, 5).toString();

                    userIdField.setText(String.valueOf(userId));
                    nameField.setText(name);
                    ageField.setText(String.valueOf(age));
                    heightField.setText(String.valueOf(height));
                    weightField.setText(String.valueOf(weight));
                    activityLevelField.setText(activityLevel);
                }
            }
        });

        pack();
        setLocationRelativeTo(null);

        displayUsers();
    }

    private void insertUser() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "kiran", "bunny")) {
            String sql = "INSERT INTO userstable (user_id, name, age, height, weight, activity_level) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(userIdField.getText()));
            statement.setString(2, nameField.getText());
            statement.setInt(3, Integer.parseInt(ageField.getText()));
            statement.setInt(4, Integer.parseInt(heightField.getText()));
            statement.setInt(5, Integer.parseInt(weightField.getText()));
            statement.setString(6, activityLevelField.getText());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                displayUsers();
                clearFields();
                JOptionPane.showMessageDialog(this, "User inserted successfully.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to insert user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifyUser() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "kiran", "bunny")) {
            String sql = "UPDATE userstable SET name = ?, age = ?, height = ?, weight = ?, activity_level = ? WHERE user_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, nameField.getText());
            statement.setInt(2, Integer.parseInt(ageField.getText()));
            statement.setInt(3, Integer.parseInt(heightField.getText()));
            statement.setInt(4, Integer.parseInt(weightField.getText()));
            statement.setString(5, activityLevelField.getText());
            statement.setInt(6, Integer.parseInt(userIdField.getText()));

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                displayUsers();
                clearFields();
                JOptionPane.showMessageDialog(this, "User modified successfully.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to modify user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "kiran", "bunny")) {
            String sql = "DELETE FROM userstable WHERE user_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(userIdField.getText()));

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                displayUsers();
                clearFields();
                JOptionPane.showMessageDialog(this, "User deleted successfully.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayUsers() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "kiran", "bunny")) {
            String sql = "SELECT user_id, name, age, height, weight, activity_level FROM userstable";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            displayTable.setModel(buildTableModel(resultSet));
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch user data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        userIdField.setText("");
        nameField.setText("");
        ageField.setText("");
        heightField.setText("");
        weightField.setText("");
        activityLevelField.setText("");
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
                new UserTable();
            }
        });
    }
}
