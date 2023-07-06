import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;


public class Food extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField foodIdField;
    private JTextField nameField;
    private JTextField calorieField;
    private JTextField carbohydrateField;
    private JTextField proteinField;
    private JTextField fatField;
    private JButton insertButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JTable displayTable;

    public Food() {
        setTitle("Food System");
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        setVisible(true);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel foodIdLabel = new JLabel("Food ID:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(foodIdLabel, constraints);

        foodIdField = new JTextField(10);
        constraints.gridx = 1;
        constraints.gridy = 0;
        add(foodIdField, constraints);

        JLabel nameLabel = new JLabel("Name:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(nameLabel, constraints);

        nameField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        add(nameField, constraints);

        JLabel calorieLabel = new JLabel("Calorie:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(calorieLabel, constraints);

        calorieField = new JTextField(10);
        constraints.gridx = 1;
        constraints.gridy = 2;
        add(calorieField, constraints);

        JLabel carbohydrateLabel = new JLabel("Carbohydrate:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        add(carbohydrateLabel, constraints);

        carbohydrateField = new JTextField(10);
        constraints.gridx = 1;
        constraints.gridy = 3;
        add(carbohydrateField, constraints);

        JLabel proteinLabel = new JLabel("Protein:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        add(proteinLabel, constraints);

        proteinField = new JTextField(10);
        constraints.gridx = 1;
        constraints.gridy = 4;
        add(proteinField, constraints);

        JLabel fatLabel = new JLabel("Fat:");
        constraints.gridx = 0;
        constraints.gridy = 5;
        add(fatLabel, constraints);

        fatField = new JTextField(10);
        constraints.gridx = 1;
        constraints.gridy = 5;
        add(fatField, constraints);

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
                insertFood();
            }
        });

        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyFood();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFood();
            }
        });

        displayTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = displayTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int foodId = Integer.parseInt(displayTable.getValueAt(selectedRow, 0).toString());
                    String name = displayTable.getValueAt(selectedRow, 1).toString();
                    int calorie = Integer.parseInt(displayTable.getValueAt(selectedRow, 2).toString());
                    int carbohydrate = Integer.parseInt(displayTable.getValueAt(selectedRow, 3).toString());
                    int protein = Integer.parseInt(displayTable.getValueAt(selectedRow, 4).toString());
                    int fat = Integer.parseInt(displayTable.getValueAt(selectedRow, 5).toString());

                    foodIdField.setText(String.valueOf(foodId));
                    nameField.setText(name);
                    calorieField.setText(String.valueOf(calorie));
                    carbohydrateField.setText(String.valueOf(carbohydrate));
                    proteinField.setText(String.valueOf(protein));
                    fatField.setText(String.valueOf(fat));
                }
            }
        });

        pack();
        setLocationRelativeTo(null);

        displayFoods();
    }

    private void insertFood() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "kiran", "bunny")) {
            String sql = "INSERT INTO food (food_id, name, calorie, carbohydrate, protein, fat) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(foodIdField.getText()));
            statement.setString(2, nameField.getText());
            statement.setInt(3, Integer.parseInt(calorieField.getText()));
            statement.setInt(4, Integer.parseInt(carbohydrateField.getText()));
            statement.setInt(5, Integer.parseInt(proteinField.getText()));
            statement.setInt(6, Integer.parseInt(fatField.getText()));

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                displayFoods();
                clearFields();
                JOptionPane.showMessageDialog(this, "Food inserted successfully.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to insert food: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifyFood() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "kiran", "bunny")) {
            String sql = "UPDATE food SET name = ?, calorie = ?, carbohydrate = ?, protein = ?, fat = ? WHERE food_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, nameField.getText());
            statement.setInt(2, Integer.parseInt(calorieField.getText()));
            statement.setInt(3, Integer.parseInt(carbohydrateField.getText()));
            statement.setInt(4, Integer.parseInt(proteinField.getText()));
            statement.setInt(5, Integer.parseInt(fatField.getText()));
            statement.setInt(6, Integer.parseInt(foodIdField.getText()));

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                displayFoods();
                clearFields();
                JOptionPane.showMessageDialog(this, "Food modified successfully.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to modify food: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteFood() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "kiran", "bunny")) {
            String sql = "DELETE FROM food WHERE food_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(foodIdField.getText()));

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                displayFoods();
                clearFields();
                JOptionPane.showMessageDialog(this, "Food deleted successfully.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete food: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayFoods() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "kiran", "bunny")) {
            String sql = "SELECT food_id, name, calorie, carbohydrate, protein, fat FROM food";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            displayTable.setModel(buildTableModel(resultSet));
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch food data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        foodIdField.setText("");
        nameField.setText("");
        calorieField.setText("");
        carbohydrateField.setText("");
        proteinField.setText("");
        fatField.setText("");
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
                new Food();
            }
        });
    }
}
