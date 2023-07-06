import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainPage extends JFrame {
    private static final long serialVersionUID = 1L;
    private JButton retrieveDetailsButton;

    public MainPage() {
        try {
            // Set Nimbus look and feel
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set frame properties
        setTitle("Food And Nutrition Suggestion System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create label
        JLabel welcomeLabel = new JLabel("Food And Nutrition Suggestion System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(welcomeLabel, BorderLayout.NORTH);

        // Create panel for the button
        JPanel buttonPanel = new JPanel();
        retrieveDetailsButton = new JButton("Retrieve Suggestions");
        buttonPanel.add(retrieveDetailsButton);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create menus
        JMenu userTableMenu = new JMenu("User Details");
        JMenu foodMenu = new JMenu("Food Details");
        JMenu mealMenu = new JMenu("Meal Details");
        JMenu healthConditionMenu = new JMenu("Condition Details");
        JMenu nutritionSuggestionMenu = new JMenu("Suggestion Details");

        // Create menu item for user menu
        JMenuItem viewUserTableDetails = new JMenuItem("View User Details");
        viewUserTableDetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new UserTable();
            }
        });

        // Create menu item for food menu
        JMenuItem viewFoodDetails = new JMenuItem("View Food Details");
        viewFoodDetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Food();
            }
        });

        // Create menu item for meal menu
        JMenuItem viewMealDetails = new JMenuItem("View Meal Details");
        viewMealDetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Meal();
            }
        });

        // Create menu item for health condition menu
        JMenuItem viewHealthConditionDetails = new JMenuItem("View Health Condition Details");
        viewHealthConditionDetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new HealthCondition();
            }
        });

        // Create menu item for nutrition suggestion menu
        JMenuItem viewNutritionSuggestionDetails = new JMenuItem("View Suggestion Details");
        viewNutritionSuggestionDetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new NutritionSuggestion();
            }
        });

        // Add menu items to respective menus
        userTableMenu.add(viewUserTableDetails);
        foodMenu.add(viewFoodDetails);
        mealMenu.add(viewMealDetails);
        healthConditionMenu.add(viewHealthConditionDetails);
        nutritionSuggestionMenu.add(viewNutritionSuggestionDetails);

        // Add menus to the menu bar
        menuBar.add(userTableMenu);
        menuBar.add(foodMenu);
        menuBar.add(mealMenu);
        menuBar.add(healthConditionMenu);
        menuBar.add(nutritionSuggestionMenu);

        // Set the menu bar
        setJMenuBar(menuBar);

        // Add the button panel to the frame
        add(buttonPanel, BorderLayout.CENTER);

        // Set button action for "Retrieve Suggestions"
        retrieveDetailsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RetrieveProgram retrieveProgram = new RetrieveProgram();
                retrieveProgram.setVisible(true);
            }
        });

        // Add window listener to handle maximizing the window
        addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent e) {
                if ((e.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
                    System.out.println("Window maximized");
                } else {
                    System.out.println("Window not maximized");
                }
            }
        });

        // Set frame size and visibility
        setSize(800, 600);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainPage();
    }
}
