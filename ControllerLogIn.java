// All the libs i use for mysql and JavaFX
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Controller class to handle all the stuff that happens on the login scene

public class ControllerLogIn 
{

    // FXML elements declared using @FXML annotation

    @FXML
    private Button cancelButton; // The cancel button

    @FXML
    private Button createNewUserButton; // The button to create a new user

    @FXML
    private Button loginButton; // The button to initiate the login process

    @FXML
    private PasswordField passwordField; // Field to input the password

    @FXML
    private TextField usernameField; // Field to input the username

    // Method for handling the cancel button action

    @FXML
    void cancel(ActionEvent event) 
    {
        System.exit(0);
    }



    // Database connection details

    private static final String url = "jdbc:mysql://localhost:3306/mydb"; // URL for the database connection
    private static final String user = "root"; // Database username
    private static final String password = "ostemad123"; // Database password

    private Connection connection; // Connection object for connecting to the database

    // Constructor to establish the database connection

    public ControllerLogIn() 
    {
        try 
        {
        // Attempt to establish the connection to the database
        connection = DriverManager.getConnection(url, user, password); // Java drive manager handles all the drivers i use to connect to the DB
        } catch (Exception e) 
        {
        // Print the stack trace if an exception occurs during the connection attempt // Stack trace viser vejen problemet.
        e.printStackTrace();
        }
    }



    @FXML
    void createNewUser(ActionEvent event) 
    {
        // Open a new window or dialog for inputting the new user's details
        // You can use JavaFX's Stage and Scene for this purpose
        Stage newUserStage = new Stage();
        newUserStage.setTitle("Create New User");
    
        // Create necessary UI components for inputting username and password
        TextField newUsernameField = new TextField();
        PasswordField newPasswordField = new PasswordField();
        Button createButton = new Button("Create");
    
        createButton.setOnAction(createEvent -> {
            // Get the input from the text fields
            String newUsername = newUsernameField.getText();
            String newPassword = newPasswordField.getText();
    
            // Validate the input fields
            if (newUsername.isEmpty() || newPassword.isEmpty()) 
            {
                showAlert("Error", "Username or password cannot be blank.");
                return;
            }
    
            // Add code to check for duplicate users
            try {
                PreparedStatement checkStatement = connection.prepareStatement("SELECT COUNT(*) FROM brugere WHERE brugernavn = ?");
                checkStatement.setString(1, newUsername);
                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);
                if (count > 0) 
                {
                showAlert("Error", "User already exists - please enter a new username and password. If this is you.");
                return;
                }
            } catch (SQLException e) 
            {
                // Handle any potential errors here
                e.printStackTrace();
                return;
            }
    
            // Add code to insert the new user's details into the database
            try {
                // Create a prepared statement
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO brugere (brugernavn, pass) VALUES (?, ?)");
                preparedStatement.setString(1, newUsername);
                preparedStatement.setString(2, newPassword);
    
                // Execute the statement
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                showAlert("Sucess", "User created sucessfully!");
                } else {
                showAlert("Error", "Failed to create user");
                }
            } catch (SQLException e) 
            {
                // Handle any potential errors here
                e.printStackTrace();
            }
    
            // Close the new user window after creating the user
            newUserStage.close();
        });
    
        // Create the layout for the new user window
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
            new Label("Username:"),
            newUsernameField,
            new Label("Password:"),
            newPasswordField,
            createButton
        );
    

    // Set the scene and show the new user window
    Scene scene = new Scene(layout, 300, 200);
    newUserStage.setScene(scene);
    newUserStage.show();
}


    // Method for handling the login button action
    @FXML
    void logIn(ActionEvent event) 
    {
        // Get the username and password entered by the user
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();

        // Query to check if the entered username and password exist in the database
        String query = "SELECT * FROM brugere WHERE BINARY brugernavn = ? AND BINARY pass = ?";

        try {
            // Prepare the statement with the query
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, enteredUsername); // Set the username parameter
            preparedStatement.setString(2, enteredPassword); // Set the password parameter

            // Execute the query and get the result set
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if the result set has any data
            if (resultSet.next()) 
            {
                // If the result set has data, it means the login was successful
                showAlert("Login Successful", "Welcome " + enteredUsername + ".");
            } 
            else 
            {
                // If the result set is empty, the login failed
                showAlert("Login Failed", "Invalid username or password.");
            }
        } catch (Exception e) 
        {
            // Print the stack trace if an exception occurs during the login process
            e.printStackTrace();
        }
    }

    // Other utility methods...

    // Method for displaying an alert
    private void showAlert(String title, String content) 
    {
        // Create an alert of type INFORMATION
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title); // Set the title of the alert
        alert.setHeaderText(null); // Set the header text of the alert
        alert.setContentText(content); // Set the content text of the alert
        alert.showAndWait(); // Display the alert and wait for user input
    }

}


