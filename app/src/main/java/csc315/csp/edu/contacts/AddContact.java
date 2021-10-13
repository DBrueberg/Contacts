/* Devin Brueberg
 * CSC 315 Programming Assignment 6
 * AddContact.java
 * October 10, 2021
 * Updated(Initials, Date, Changes):
 *  (DAB, 10/11/2021, Added Comments)
 *
 */
// Package of the program
package csc315.csp.edu.contacts;

// Importing needed android/java modules
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

/**
 * The AddContact activity will use a form with all the needed
 * contact data entries. It will then add a new entry to the
 * database with the entered data using the DatabaseHelper.
 *
 */
public class AddContact extends AppCompatActivity {
    // Initializing a new DatabaseHelper object of null
    // value
    private DatabaseHelper db = null;


    /**
     * The onCreate method will initialize a reference to the database
     * object and create all needed listeners that will allow a contact
     * to be added.
     *
     * @param savedInstanceState - Bundle of the activity data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Calling the super constructor and setting the main layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // Starting up a database instance that will be used to access the
        // database
        db = DatabaseHelper.getInstance(getApplicationContext());

        // Initializing a reference to the saveButton Button object that will
        // be used to create a listener
        Button saveButton = findViewById(R.id.addSaveButton);
        // The saveButton listeners will retrieve all values from the view
        // fields and call the database addContact method to add the
        // contact to the database. The activity will then be closed.
        saveButton.setOnClickListener(view -> {
            // Creating references to the field views.
            EditText nameView = findViewById(R.id.addNameEditTextView);
            EditText phoneView = findViewById(R.id.addPhoneEditTextView);
            EditText emailView = findViewById(R.id.addEmailEditTextView);
            EditText addressView = findViewById(R.id.addAddressEditTextView);
            EditText notesView = findViewById(R.id.addNotesEditTextView);

            // Retrieving the data in each field view and converting to
            // Strings
            String name = nameView.getText().toString();
            String phone = phoneView.getText().toString();
            String email = emailView.getText().toString();
            String address = addressView.getText().toString();
            String notes = notesView.getText().toString();

            // If name has no value nothing will happen, but if it does
            // have a value then the contact is added
            if (!name.equals("")) {
                db.addContact(name, phone, email, address, notes);

                // The activity is terminated
                finish();
            }

        });

        // Initializing a reference to the cancelButton Button object that will
        // be used to create a listener
        Button cancelButton = findViewById(R.id.addCancelButton);
        // The cancelButton will simply close out the activity
        cancelButton.setOnClickListener(view -> {
            finish();
            // DEBUG METHOD
//            db.deleteAll();
        });
    }
}