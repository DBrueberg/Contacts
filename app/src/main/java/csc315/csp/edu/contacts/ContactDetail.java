/* Devin Brueberg
 * CSC 315 Programming Assignment 6
 * ContactDetail.java
 * October 10, 2021
 * Updated(Initials, Date, Changes):
 *  (DAB, 10/11/2021, Added mapView and comments)
 *  (DAB, 10/12/2021, Made the geocoder ASYNC, ironed
 *  out an empty string call bug, and enhanced the intent
 *  process by overriding onRequestPermissionsResult)
 *
 */
// Package of the program
package csc315.csp.edu.contacts;

// Importing needed android/java modules
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The ContactDetail Activity will allow for the updating of contacts via
 * the update button at the bottom of the screen. The user will be able to
 * write emails or calls if they are supported and permissions are given. A map
 * will be displayed of the current users location if one is found.
 *
 */
public class ContactDetail extends AppCompatActivity implements OnMapReadyCallback {
    // API key to use google maps with this application
    private static final String MAPVIEW_BUNDLE_KEY = "AIzaSyDAJJaTXmD0QG9gBZdG7E6eOQT17NpI5qE";
    private static final int DIAL_REQUEST_CODE = 101;
    // The variables are declared that will allow access to the database and
    // mapView
    private DatabaseHelper db = null;
    private ArrayList<String> contact;
    private MapView mMapView;


    /**
     * The onCreate method will set up a database instance, retrieve the bundle
     * data, set up listeners, and initialize the map view.
     *
     * @param savedInstanceState - saved Bundle object.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Calling the super constructor and setting the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        // Referencing the database instance currently running and
        // creating a reference to that instance
        db = DatabaseHelper.getInstance(getApplicationContext());

        // Retrieving the Bundle object from the Intent
        Bundle extras = getIntent().getExtras();

        // If there are Bundle objects, the contact ArrayList will
        // be assigned to contact
        if (extras != null) {
            contact = extras.getStringArrayList("contact");
        }

        // Setting up the listeners and map for this activity
        listenerSetUp();
        initGoogleMap(savedInstanceState);
    }


    // ******************MAIN VIEW OVERRIDES*************
    /**
     * Overriding the onRequestPermissionsResult method to uniquely
     * handle intents that need special treatment. Currently only
     * the DIAL_REQUEST_CODE is handled differently
     *
     * @param requestCode - int request code of the intent.
     * @param permissions - String permissions requested.
     * @param grantResults - Results of permissions, can not be null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        if (requestCode != DIAL_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions,
                    grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
            callIntent("<Number>");
            return;
        }
    }


    /**
     * The activity bundle will be saved for later use
     *
     * @param outState - The application Bundle object.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Calling the super constructor
        super.onSaveInstanceState(outState);

        // Retrieving the map bundle via the key reference and
        // saving a new bundle reference with the updated data
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        // Saving the map bundle
        mMapView.onSaveInstanceState(mapViewBundle);
    }


    // ******************MAP VIEW OVERRIDES*************
    /**
     * Resume lifecycle method for the mapView.
     */
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }


    /**
     * onStart lifecycle method for the mapView.
     */
    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }


    /**
     * onStop lifecycle method for the mapView.
     */
    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }


    /**
     * onMapReady lifecycle method for the mapView. The
     * map object will be updated with current view and
     * location data.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        moveMap(map);
    }


    /**
     * onPause lifecycle method for the mapView.
     */
    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }


    /**
     * onDestroy lifecycle method for the mapView.
     */
    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }


    /**
     * onLowMemory lifecycle method for the mapView.
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    // ******************ACTIVITY METHODS*************

    /**
     * The callIntent method will build and sent a call intent with a
     * String number.
     *
     * @param number - String number to call
     */
    public void callIntent(String number){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(callIntent);
    }


    /**
     * The checkPermission method will check to see that the application has
     * the correct permissions when passed a permission and request code. The
     * method will return a boolean with true, permissions granted, or false,
     * permissions not granted.
     *
     * @param permission - android constant permission.
     * @param requestCode - request code when asking for the permission.
     * @return - true if permission granted, false if not granted.
     */
    public boolean checkPermission(String permission, int requestCode) {
        // Initializing a boolean value that will contain the result of asking for
        // the specified permission
        boolean isValidPermission =
                ContextCompat.checkSelfPermission(ContactDetail.this, permission) ==
                        PackageManager.PERMISSION_GRANTED;

        // If the permission is not granted, the application will ask for it
        if (!isValidPermission) {
            ActivityCompat.requestPermissions(this, new String[] {permission}, requestCode);
            // Rechecking permissions to return a valid true or false result
            isValidPermission = ContextCompat.checkSelfPermission(ContactDetail.this, permission) ==
                    PackageManager.PERMISSION_GRANTED;
        }
        // Return the boolean value indicating if permission was granted or not
        return isValidPermission;
    }


    /**
     * The listenerSetUp method will set up the listeners for this application
     */
    private void listenerSetUp() {
        // Referencing all the required views
        EditText nameEditView = findViewById(R.id.detailNameEditTextView);
        EditText phoneEditView = findViewById(R.id.detailPhoneEditTextView);
        EditText emailEditView = findViewById(R.id.detailEmailEditTextView);
        EditText addressEditView = findViewById(R.id.detailAddressEditTextView);
        EditText notesEditView = findViewById(R.id.detailNotesEditTextView);

        TextView nameTextView = findViewById(R.id.detailNameTextView);
        TextView phoneTextView = findViewById(R.id.detailPhoneTextView);
        TextView emailTextView = findViewById(R.id.detailEmailTextView);
        TextView addressTextView = findViewById(R.id.detailAddressTextView);
        TextView notesTextView = findViewById(R.id.detailNotesTextView);

        // Retrieving the contact information from the contact list
        String name = contact.get(1);
        String phone = contact.get(2);;
        String email = contact.get(3);;
        String address = contact.get(4);;
        String notes = contact.get(5);;

        // Setting the current data to the views
        nameEditView.setText(name);
        phoneEditView.setText(phone);
        emailEditView.setText(email);
        addressEditView.setText(address);
        notesEditView.setText(notes);

        nameTextView.setText(name);
        phoneTextView.setText(phone);
        emailTextView.setText(email);
        addressTextView.setText(address);
        notesTextView.setText(notes);

        // Initializing a variable in reference to the callButton
        Button callButton = findViewById(R.id.detailCallButton);
        // The callButton listener will first request permissions to call if they are needed,
        // then will place the call if permissions are granted
        callButton.setOnClickListener(view -> {
            // Checking if permissions are granted or not, if granted calling the callIntent and that
            // the phone is not an empty string (The device will crash)
            if (checkPermission(Manifest.permission.CALL_PHONE, DIAL_REQUEST_CODE) && !phone.equals("")) {
                // Calling the callIntent method to send out the intent
                callIntent(phone);
            }
            else {
                // If phone number is empty string the user is notified
                if (phone.equals("")) {
                    Toast.makeText(this, "Please add a contact number", Toast.LENGTH_SHORT).show();
                }
                // If phone does not have permissions the user is notified
                else {
                    Toast.makeText(this, "Check Permissions", Toast.LENGTH_SHORT).show();
                }

            }

        });

        // Initializing a variable in reference to the emailButton
        Button emailButton = findViewById(R.id.detailEmailButton);
        // The emailButton listener will first request permissions to call if they are needed,
        // then will open the email if permissions are granted
        emailButton.setOnClickListener(view -> {
            // Initializing the intent and putting the address and subject before
            // starting the activity if it is available
            Intent i = new Intent(Intent.ACTION_SENDTO);
            i.setData(Uri.parse("mailto:"));
            i.putExtra(i.EXTRA_EMAIL, email);
            i.putExtra(i.EXTRA_SUBJECT, "new");
            if (getIntent().resolveActivity(getPackageManager()) != null) {
                startActivity(i);
            }
        });

        // Initializing a variable in reference to the updateButton
        Button updateButton = findViewById(R.id.detailUpdateButton);
        // The updateButton listener will retrieve the values in the
        // contact detail form and update the database accordingly
        // via the databaseHelper update method. The activity will
        // be closed after update
        updateButton.setOnClickListener(view -> {
            // Initializing the empty ArrayList<String> to hold the contact
            ArrayList<String> contactUpdate = new ArrayList<>();

            // Retrieving the contact from the EditView
            contactUpdate.add(contact.get(0));
            contactUpdate.add(nameEditView.getText().toString());
            contactUpdate.add(phoneEditView.getText().toString());
            contactUpdate.add(emailEditView.getText().toString());
            contactUpdate.add(addressEditView.getText().toString());
            contactUpdate.add(notesEditView.getText().toString());

            // Updating database
            db.update(contactUpdate);

            // Closing out the activity
            finish();
        });
    }


    /**
     * The initGoogleMap method will initialize the google map view.
     *
     * @param savedInstanceState - saved object bundle.
     */
    private void initGoogleMap(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        // Initializing the map bundle with a null value
        Bundle mapViewBundle = null;

        // If there is a previous saved instance mapViewBundle is assigned that value
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        // Referencing the mapView object and creating the map from
        // the bundle
        mMapView = findViewById(R.id.detailMapView);
        mMapView.onCreate(mapViewBundle);

        // Calls the object when the GoogleMap instance is ready to
        // be used
        mMapView.getMapAsync(this);
    }


    /**
     * Checking to verify that the requested intent is available to be used. If
     * there is a result it is ready, if not it is not ready.
     *
     * @param intent - intent to be checked.
     * @return - return true or false depending on if the resource is available.
     */
    public boolean isIntentAvailable(Intent intent) {
        // Checking if there is any application that can handle the intent and saving the int
        // quantity activityAvailable
        int activityAvailable =
                getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size();

        // Returning true if there are activities available and false if not
        return activityAvailable > 0;
    }


    /**
     * The moveMap method will move the location in the map view to that
     * of the address location of the currently viewed contact.
     *
     * @param map - GoogleMap reference.
     */
    public void moveMap(GoogleMap map) {
        // Retrieving the address and name of the contact
        String address = contact.get(4);
        String name = contact.get(1);

        // Geocoder takes a while to run so creating a thread used only
        // for the location retrieval
        AsyncTask.execute(new Runnable() {
            // The run method is overridden to let geocoder perform the location retrieval
            // from a new thread. It only performs a move on the map so it
            // is not critical.
            @Override
            public void run() {
                // Declaring a position object to hold the position
                LatLng position;
                // Declaring latitude and longitude doubles to
                // be used to hold position data
                double latitude;
                double longitude;

                // Initializing a List<Address> to hold the results of the upcoming geocode
                // query
                List<Address> geocodeMatches = null;
                // If there address is not an empty string
                if (!address.equals("")) {
                    try {
                        // A GeoCoder search is performed and the first result is saved in the array
                        geocodeMatches =
                                new Geocoder(getApplicationContext()).getFromLocationName(address, 1);
                    }
                    // If there was an exception it is handled
                    catch (IOException e) {
                        // Geocoder does not work well so specific message is made to outline this issue
                        if (e.getMessage().equals("grpc failed")) {
                            System.out.println("*****CHECK YOUR INTERNET - GEOCODER COULD NOT DOWNLOAD FAST ENOUGH");
                            // e.printStackTrace();
                        }
                        // Else it is an unknown issue and is printed to logs
                        else {
                            System.out.println("In geocoder " + e);
                        }
                    }

                    // If there was a geocodeMatch result it will be used to move the camera
                    // to the desired location and set a marker with the contacts name
                    if (geocodeMatches != null && !geocodeMatches.isEmpty()) {
                        // Retrieving the lat and long from the geocodeMatches list
                        latitude = geocodeMatches.get(0).getLatitude();
                        longitude = geocodeMatches.get(0).getLongitude();

                        // Creating a new LatLng position object
                        position = new LatLng(latitude, longitude);

                        // Updating the Views needs to be done on the UI thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Adding a marker of where the contact is on the map
                                map.addMarker(new MarkerOptions()
                                        .position(position)
                                        .title(name));
                                // Building a new CameraUpdateFactory object to move the map position
                                // and zoom in by 10
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
                            }
                        });
                    }
                }
            }
        });
    }
}