import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class Meal extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField mealIdField;
    private JTextField userIdField;
    private JTextField foodIdField;
    private JTextField quantityField;
    private JTextField mealDateField;
    private JButton insertButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JTable displayTable;

    public Meal() {
        setTitle("Meal System");
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        setVisible(true);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel mealIdLabel = new JLabel("Meal ID:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(mealIdLabel, constraints);

        mealIdField = new JTextField(10);
        constraints.gridx = 1;
        constraints.gridy = 0;
        add(mealIdField, constraints);

        JLabel userIdLabel = new JLabel("User ID:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(userIdLabel, constraints);

        userIdField = new JTextField(10);
        constraints.gridx = 1;
        constraints.gridy = 1;
        add(userIdField, constraints);

        JLabel foodIdLabel = new JLabel("Food ID:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(foodIdLabel, constraints);

        foodIdField = new JTextField(10);
        constraints.gridx = 1;
        constraints.gridy = 2;
        add(foodIdField, constraints);

        JLabel quantityLabel = new JLabel("Quantity:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        add(quantityLabel, constraints);

        quantityField = new JTextField(10);
        constraints.gridx = 1;
        constraints.gridy = 3;
        add(quantityField, constraints);

        JLabel mealDateLabel = new JLabel("Meal Date:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        add(mealDateLabel, constraints);

        mealDateField = new JTextField(10);
        constraints.gridx = 1;
        constraints.gridy = 4;
        add(mealDateField, constraints);

        insertButton = new JButton("Insert");
        constraints.gridx = 0;
        constraints.gridy = 5;
        add(insertButton, constraints);

        modifyButton = new JButton("Modify");
        constraints.gridx = 1;
        constraints.gridy = 5;
        add(modifyButton, constraints);

        deleteButton = new JButton("Delete");
        constraints.gridx = 2;
        constraints.gridy = 5;
        add(deleteButton, constraints);

        displayTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(displayTable);
        constraints.gridx = 0;
        constraints.gridy = 6;
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
                insertMeal();
            }
        });

        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyMeal();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMeal();
            }
        });

        displayTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = displayTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int mealId = Integer.parseInt(displayTable.getValueAt(selectedRow, 0).toString());
                    int userId = Integer.parseInt(displayTable.getValueAt(selectedRow, 1).toString());
                    int foodId = Integer.parseInt(displayTable.getValueAt(selectedRow, 2).toString());
                    int quantity = Integer.parseInt(displayTable.getValueAt(selectedRow, 3).toString());
                    String mealDate = displayTable.getValueAt(selectedRow, 4).toString();

                    mealIdField.setText(String.valueOf(mealId));
                    userIdField.setText(String.valueOf(userId));
                    foodIdField.setText(String.valueOf(foodId));
                    quantityField.setText(String.valueOf(quantity));
                    mealDateField.setText(mealDate);
                }
            }
        });

        pack();
        setLocationRelativeTo(null);

        displayMeals();
    }

    private void insertMeal() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "kiran", "bunny")) {
            String sql = "INSERT INTO meal (meal_id, user_id, food_id, quantity, meal_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(mealIdField.getText()));
            statement.setInt(2, Integer.parseInt(userIdField.getText()));
            statement.setInt(3, Integer.parseInt(foodIdField.getText()));
            statement.setInt(4, Integer.parseInt(quantityField.getText()));
            statement.setString(5, mealDateField.getText());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                displayMeals();
                clearFields();
                JOptionPane.showMessageDialog(this, "Meal inserted successfully.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to insert meal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifyMeal() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "kiran", "bunny")) {
            String sql = "UPDATE meal SET user_id = ?, food_id = ?, quantity = ?, meal_date = ? WHERE meal_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(userIdField.getText()));
            statement.setInt(2, Integer.parseInt(foodIdField.getText()));
            statement.setInt(3, Integer.parseInt(quantityField.getText()));
            statement.setString(4, mealDateField.getText());
            statement.setInt(5, Integer.parseInt(mealIdField.getText()));

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                displayMeals();
                clearFields();
                JOptionPane.showMessageDialog(this, "Meal modified successfully.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to modify meal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMeal() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "kiran", "bunny")) {
            String sql = "DELETE FROM meal WHERE meal_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(mealIdField.getText()));

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                displayMeals();
                clearFields();
                JOptionPane.showMessageDialog(this, "Meal deleted successfully.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete meal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayMeals() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "kiran", "bunny")) {
            String sql = "SELECT meal_id, user_id, food_id, quantity, meal_date FROM meal";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            displayTable.setModel(buildTableModel(resultSet));
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch meal data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        mealIdField.setText("");
        userIdField.setText("");
        foodIdField.setText("");
        quantityField.setText("");
        mealDateField.setText("");
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
                new Meal();
            }
        });
    }
}

//Note: Remember to replace the database connection details ("jdbc:oracle:thin:@localhost:1521:xe", "kiran", "bunny") with your own database credentials.
