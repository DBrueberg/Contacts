/* Devin Brueberg
 * CSC 315 Programming Assignment 6
 * MainActivity.java
 * October 10, 2021
 * Updated(Initials, Date, Changes):
 *  (DAB, 10/11/2021, Added Comments)
 *
 */
// Package of the program
package csc315.csp.edu.contacts;

// Importing needed android/java modules
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * Main Activity of the application. From here intents will be called to allow for
 * navigation through the Contact application. A list of contacts will be
 * displayed on the screen. They may be edited by clicking on one or added
 * by clicking the plus FAB button.
 *
 */
public class MainActivity extends AppCompatActivity {
    // Initializing of the list view and the database instance
    private ArrayList<ArrayList<String>> contactList = null;
    private MyListAdapter arrayAdapter = null;
    private ListView contactView = null;
    private DatabaseHelper dbHelper = null;


    /**
     * The onCreate method sets up the onClick actions and populates the
     * listview with added contacts.
     *
     * @param savedInstanceState - Holds the data for the application if any
     *                           was packaged into a bundle for lifetime
     *                           cycle actions.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Calling the super constructor and setting the main layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Starting up a database instance that will be used to access the
        // database
        dbHelper = DatabaseHelper.getInstance(getApplicationContext());

        // Retrieving all the contacts stored in the database
        contactList = dbHelper.getAllContacts();

        // Setting up the custom array adapter, loading in the custom view, and
        // setting the adapter to display the contactList
        arrayAdapter = new MyListAdapter(this, contactList);
        contactView = findViewById(R.id.mainContactListView);
        contactView.setAdapter(arrayAdapter);

        // The listener will allow for the processing of clicks on the displayed listview.
        // When clicked, a explicit intent will be fired to load up the ContactDetail
        // activity that will allow the contacts to be edited.
        contactView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Retrieving the selected contactList contact and saving it to contact
               ArrayList<String> contact = (ArrayList<String>) parent.getItemAtPosition(position);

               // Writing the intent and putting the contact array in to be sent over before
               // starting the intent
               Intent i = new Intent(MainActivity.this, ContactDetail.class);
               i.putExtra("contact", contact);
               startActivity(i);
           }
       });

        // Referencing the FAB and setting a listener that will trigger a
        // explicit intent that will load up the AddContact application
        FloatingActionButton fab = findViewById(R.id.mainFAB);
        fab.setOnClickListener(view -> {
            // The intent will load up the AddContact page
            Intent i = new Intent(MainActivity.this, AddContact.class);
            startActivity(i);
        });
    }


    /**
     * If the application is paused then the contactList will need to be refreshed
     * onResume.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Clearing the contact list and adding the newly retrieved contactList
        // to be referenced. The arrayAdapter is notified of the new list
        contactList.clear();
        contactList.addAll(dbHelper.getAllContacts());
        arrayAdapter.notifyDataSetChanged();
    }
}