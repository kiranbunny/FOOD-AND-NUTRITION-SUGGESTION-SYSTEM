import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RetrieveProgram extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Database connection details
    private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USERNAME = "kiran";
    private static final String PASSWORD = "bunny";

    private JLabel userIdLabel;
    private JTextField userIdField;
    private JButton retrieveButton;
    private JTextArea healthConditionArea;
    private JTextArea suggestionArea;

    public RetrieveProgram() {
        setTitle("Nutrition Management System - Retrieve");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Create form controls
        userIdLabel = new JLabel("User ID:");
        userIdField = new JTextField(10);
        retrieveButton = new JButton("Retrieve");
        healthConditionArea = new JTextArea(20, 50);
        suggestionArea = new JTextArea(20, 50);
        healthConditionArea.setEditable(false);
        suggestionArea.setEditable(false);

        // Add action listener to retrieve button
        retrieveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int userId = Integer.parseInt(userIdField.getText());
                retrieveData(userId);
            }
        });

        // Add components to the frame
        add(userIdLabel);
        add(userIdField);
        add(retrieveButton);
        //add(new JScrollPane(healthConditionArea,));
        //add(new JScrollPane(suggestionArea));
        add(new JScrollPane(healthConditionArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
        add(new JScrollPane(suggestionArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));


        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private void retrieveData(int userId) {
        try {
            // Establishing database connection
            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            // Retrieve user information
            String userQuery = "SELECT * FROM Users WHERE user_id = ?";
            PreparedStatement userStatement = connection.prepareStatement(userQuery);
            userStatement.setInt(1, userId);
            ResultSet userResultSet = userStatement.executeQuery();

            if (userResultSet.next()) {
                String userName = userResultSet.getString("name");
                int age = userResultSet.getInt("age");
                String gender = userResultSet.getString("gender");
                int height = userResultSet.getInt("height");
                int weight = userResultSet.getInt("weight");
                String activityLevel = userResultSet.getString("activity_level");

                // Set user information
                healthConditionArea.setText("User Information:\n");
                healthConditionArea.append("Name: " + userName + "\n");
                healthConditionArea.append("Age: " + age + "\n");
                healthConditionArea.append("Gender: " + gender + "\n");
                healthConditionArea.append("Height: " + height + "\n");
                healthConditionArea.append("Weight: " + weight + "\n");
                healthConditionArea.append("Activity Level: " + activityLevel + "\n");

                // Retrieve health condition information
                String healthConditionQuery = "SELECT * FROM Healthcondition WHERE user_id = ?";
                PreparedStatement healthConditionStatement = connection.prepareStatement(healthConditionQuery);
                healthConditionStatement.setInt(1, userId);
                ResultSet healthConditionResultSet = healthConditionStatement.executeQuery();

                if (healthConditionResultSet.next()) {
                    int healthConditionId = healthConditionResultSet.getInt("healthcondition_id");
                    String conditionName = healthConditionResultSet.getString("condition_name");
                    String conditionDescription = healthConditionResultSet.getString("condition_description");

                    // Append health condition information
                    healthConditionArea.append("\nHealth Condition:\n");
                    healthConditionArea.append("Condition ID: " + healthConditionId + "\n");
                    healthConditionArea.append("Condition Name: " + conditionName + "\n");
                    healthConditionArea.append("Condition Description: " + conditionDescription + "\n");
                } else {
                    healthConditionArea.append("\nNo health condition found for the user.");
                }

                // Retrieve nutrition suggestion information
                String nutritionSuggestionQuery = "SELECT * FROM NutritionSuggestion WHERE user_id = ?";
                PreparedStatement nutritionSuggestionStatement = connection.prepareStatement(nutritionSuggestionQuery);
                nutritionSuggestionStatement.setInt(1, userId);
                ResultSet nutritionSuggestionResultSet = nutritionSuggestionStatement.executeQuery();

                if (nutritionSuggestionResultSet.next()) {
                    int suggestionId = nutritionSuggestionResultSet.getInt("suggestion_id");
                    String suggestion = nutritionSuggestionResultSet.getString("suggestion");
                    String suggestionDate = nutritionSuggestionResultSet.getString("suggestion_date");
                    String diet = nutritionSuggestionResultSet.getString("diet");

                    // Set nutrition suggestion information
                    suggestionArea.setText("Nutrition Suggestion:\n");
                    suggestionArea.append("Suggestion ID: " + suggestionId + "\n");
                    suggestionArea.append("Suggestion: " + suggestion + "\n");
                    suggestionArea.append("Suggestion Date: " + suggestionDate + "\n");
                    suggestionArea.append("Diet: " + diet + "\n");
                } else {
                    suggestionArea.setText("No nutrition suggestion found for the user.");
                }

            } else {
                healthConditionArea.setText("No user found with the specified user ID.");
                suggestionArea.setText("");
            }

            // Closing database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RetrieveProgram().setVisible(true);
            }
        });
    }
}
