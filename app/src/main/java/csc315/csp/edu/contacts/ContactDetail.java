/* Devin Brueberg
 * CSC 315 Programming Assignment 6
 * ContactDetail.java
 * October 10, 2021
 * Updated(Initials, Date, Changes):
 *  (DAB, 10/11/2021, Added mapView and comments)
 *
 */
// Package of the program
package csc315.csp.edu.contacts;

// Importing needed android/java modules
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.widget.*;
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
        db = DatabaseHelper.getInstance(this);

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
        }
        // Else the permission was granted and the user is notified
        else {
            Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show();
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

        // Setting the current data to the views
        nameEditView.setText(contact.get(1));
        phoneEditView.setText(contact.get(2));
        emailEditView.setText(contact.get(3));
        addressEditView.setText(contact.get(4));
        notesEditView.setText(contact.get(5));

        nameTextView.setText(contact.get(1));
        phoneTextView.setText(contact.get(2));
        emailTextView.setText(contact.get(3));
        addressTextView.setText(contact.get(4));
        notesTextView.setText(contact.get(5));

        // Initializing a variable in reference to the callButton
        Button callButton = findViewById(R.id.detailCallButton);
        // The callButton listener will first request permissions to call if they are needed,
        // then will place the call if permissions are granted
        callButton.setOnClickListener(view -> {
            // Saving the phone number to the phone variable
            String phone = phoneEditView.getText().toString();

            // Using the variable isValidPermission to check and add the permission
            if (checkPermission(Manifest.permission.CALL_PHONE, 102)) {
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                if (getIntent().resolveActivity(getPackageManager()) != null) {
                    startActivity(i);
                }
            }
        });

        // Initializing a variable in reference to the emailButton
        Button emailButton = findViewById(R.id.detailEmailButton);
        // The emailButton listener will first request permissions to call if they are needed,
        // then will open the email if permissions are granted
        emailButton.setOnClickListener(view -> {
            // Saving the email number to the email variable
            String email = emailEditView.getText().toString();

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
            ArrayList<String> contactUpdate = new ArrayList<>();
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
     * @param context - context of the application.
     * @param action - action wanting to be performed.
     * @return - return true or false depending on if the resource is available.
     */
    public static boolean isIntentAvailable(Context context, String action) {
        // Initializing the PackageManager and an Intent with the action
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);

        // Checking if there is any application that can handle the intent
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        // If there are items in the list then the intent can be handled and true is returned
        return list.size() > 0;
    }

    /**
     * The moveMap method will move the location in the map view to that
     * of the address location of the currently viewed contact.
     *
     * @param map - GoogleMap reference.
     */
    public void moveMap(GoogleMap map) {
        // Declaring latitude and longitude doubles to
        // be used to hold position data
        double latitude;
        double longitude;
        // Declaring a position object to hold the position
        LatLng position;
        // Retrieving the address and name of the contact
        String address = contact.get(4);
        String name = contact.get(1);

        // Initializing a List<Address> to hold the results of the upcoming geocode
        // query
        List<Address> geocodeMatches = null;
        // If there address is not an empty string
        if (!address.equals("")) {
            try {
                // A GeoCoder search is performed and the first result is saved in the array
                geocodeMatches =
                        new Geocoder(this).getFromLocationName(address, 1);
            }
            // If there was an exception it is handled
            catch (IOException e) {
                // Geocoder does not work well so specific message is made to outline this issue
                if (e.getMessage().equals("grpc failed")) {
                    System.out.println("*****CHECK YOUR INTERNET - GEOCODER COULD NOT DOWNLOAD FAST ENOUGH");
                }
                // Else it is an unknown issue and is printed to logs
                else {
                    System.out.println("In geocoder " + e);
                }
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
            // Adding a marker of where the contact is on the map
            map.addMarker(new MarkerOptions()
                    .position(position)
                    .title(name));
            // Building a new CameraUpdateFactory object to move the map position
            // and zoom in by 10
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
        }
    }
}