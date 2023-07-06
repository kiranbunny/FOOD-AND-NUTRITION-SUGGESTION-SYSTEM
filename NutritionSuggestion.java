import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class NutritionSuggestion extends JFrame {
    private JTextField suggestionIdField;
    private JTextField userIdField;
    private JTextField suggestionField;
    private JTextField suggestionDateField;
    private JTextField dietField;
    private JButton insertButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JTable displayTable;

    public NutritionSuggestion() {
        setTitle("Nutrition Suggestion System");
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        setVisible(true);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel suggestionIdLabel = new JLabel("Suggestion ID:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(suggestionIdLabel, constraints);

        suggestionIdField = new JTextField(40);
        constraints.gridx = 1;
        constraints.gridy = 0;
        add(suggestionIdField, constraints);

        JLabel userIdLabel = new JLabel("User ID:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(userIdLabel, constraints);

        userIdField = new JTextField(40);
        constraints.gridx = 1;
        constraints.gridy = 1;
        add(userIdField, constraints);

        JLabel suggestionLabel = new JLabel("Suggestion:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(suggestionLabel, constraints);

        suggestionField = new JTextField(40);
        constraints.gridx = 1;
        constraints.gridy = 2;
        add(suggestionField, constraints);

        JLabel suggestionDateLabel = new JLabel("Suggestion Date:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        add(suggestionDateLabel, constraints);

        suggestionDateField = new JTextField(40);
        constraints.gridx = 1;
        constraints.gridy = 3;
        add(suggestionDateField, constraints);

        JLabel dietLabel = new JLabel("Diet:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        add(dietLabel, constraints);

        dietField = new JTextField(40);
        constraints.gridx = 1;
        constraints.gridy = 4;
        add(dietField, constraints);

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
                insertSuggestion();
            }
        });

        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifySuggestion();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               deleteSuggestion();
            }
        });

        displayTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = displayTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int suggestionId = Integer.parseInt(displayTable.getValueAt(selectedRow, 0).toString());
                    int userId = Integer.parseInt(displayTable.getValueAt(selectedRow, 1).toString());
                    String suggestion = displayTable.getValueAt(selectedRow, 2).toString();
                    String suggestionDate = displayTable.getValueAt(selectedRow, 3).toString();
                    String diet = displayTable.getValueAt(selectedRow, 4).toString();

                    suggestionIdField.setText(String.valueOf(suggestionId));
                    userIdField.setText(String.valueOf(userId));
                    suggestionField.setText(suggestion);
                    suggestionDateField.setText(suggestionDate);
                    dietField.setText(diet);
                }
            }
        });

        pack();
        setLocationRelativeTo(null);

        displaySuggestions();
    }

    private void insertSuggestion() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "kiran", "bunny")) {
            String sql = "INSERT INTO nutritionsuggestion (suggestion_id, user_id, suggestion, suggestion_date, diet) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(suggestionIdField.getText()));
            statement.setInt(2, Integer.parseInt(userIdField.getText()));
            statement.setString(3, suggestionField.getText());
            statement.setString(4, suggestionDateField.getText());
            statement.setString(5, dietField.getText());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                displaySuggestions();
                clearFields();
                JOptionPane.showMessageDialog(this, "Suggestion inserted successfully.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to insert suggestion: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifySuggestion() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "kiran", "bunny")) {
            String sql = "UPDATE nutritionsuggestion SET user_id = ?, suggestion = ?, suggestion_date = to_date(?, 'YYYY-MM-DD'), diet = ? WHERE suggestion_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(userIdField.getText()));
            statement.setString(2, suggestionField.getText());
            statement.setString(3, suggestionDateField.getText());
            statement.setString(4, dietField.getText());
            statement.setInt(5, Integer.parseInt(suggestionIdField.getText()));

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                displaySuggestions();
                clearFields();
                JOptionPane.showMessageDialog(this, "Suggestion modified successfully.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to modify suggestion: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSuggestion() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "kiran", "bunny")) {
            String sql = "DELETE FROM nutritionsuggestion WHERE suggestion_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(suggestionIdField.getText()));

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                displaySuggestions();
                clearFields();
                JOptionPane.showMessageDialog(this, "Suggestion deleted successfully.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete suggestion: " +ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displaySuggestions() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "kiran", "bunny")) {
            String sql = "SELECT suggestion_id, user_id, suggestion, suggestion_date, diet FROM nutritionsuggestion";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            displayTable.setModel(buildTableModel(resultSet));
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch suggestion data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        suggestionIdField.setText("");
        userIdField.setText("");
        suggestionField.setText("");
        suggestionDateField.setText("");
        dietField.setText("");
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
                new NutritionSuggestion();
            }
        });
    }
}
