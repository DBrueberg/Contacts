/* Devin Brueberg
 * CSC 315 Programming Assignment 6
 * DatabaseHelper.java
 * October 10, 2021
 * Updated(Initials, Date, Changes):
 *  (DAB, 10/11/2021, Added Comments)
 *
 */
// Package of the program
package csc315.csp.edu.contacts;

// Importing needed android/java modules
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * The DatabaseHelper class uses the singleton method to
 * create database access that will create one instance that
 * can then be referenced from any activity using the
 * static getInstance() method.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // Initializing final variable for the database name,
    // table name, and version
    private final static String DB_NAME = "contactList";
    private final static String TABLE_NAME = "contact_table";
    private final static int VERSION = 1;
    // Declaring a DatabaseHelper instance
    private static DatabaseHelper db = null;


    /**
     * Using the singleton databaseHelper method. This will synchronize
     * and ensure only one instance is running at any time. A reference
     * to that instance will be passes when attempts are made and the
     * helper is already running.
     *
     * @param context - context of the application.
     * @return - A reference to a DatabaseHelper instance.
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        // If the DatabaseHelper is null a new instance is made and returned
        if (db == null) {
            db = new DatabaseHelper(context.getApplicationContext());
        }
        // Else the current one is simply returned
        return db;
    }


    /**
     * Constructor that will construct a new DatabaseHelper instance.
     *
     * @param context - Context of the application.
     */
    private DatabaseHelper(@Nullable Context context) {
        // Passing the name and version to the super constructor in
        // order to create the database
        super(context, DB_NAME, null, VERSION);
    }


    /**
     * The onCreate method will add a new table to the database with
     * all the required fields.
     *
     * @param db - An SQLiteDatabase instance reference.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Using the execSQL() command to create a new table
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                " (id integer primary key autoincrement, name text, phone text, " +
                "email text, address text, notes text)");
    }


    /**
     * The onDowngrade() method is overridden to allow the contactList
     * database to be downgraded to an old version if desired. The
     * new table is dropped and the onCreate() method is called
     * to create the desired one.
     *
     * @param db - An instance of SQLiteDatabase.
     * @param prevVersion - Previous int version of the database.
     * @param newVersion - New int version of the database.
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int prevVersion, int newVersion) {
        // Calling execSQL to execute a statement to drop the current table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Calling the onCreate() method to create the new version of the
        // database
        onCreate(db);
    }


    /**
     * The onUpgrade() method is overridden to allow the todoList
     * database to be upgraded to a new version if desired. The
     * old table is dropped and the onCreate() method is called
     * to create the new one.
     *
     * @param db - An instance of SQLiteDatabase.
     * @param prevVersion - Previous int version of the database.
     * @param newVersion - New int version of the database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int prevVersion, int newVersion) {
        // Calling execSQL to execute a statement to drop the current table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Calling the onCreate() method to create the new version of the
        // database
        onCreate(db);
    }


    /**
     * The addContact method will accept the String values for all of the
     * table fields and create a new contact entry in the table.
     *
     * @param name - String contact name.
     * @param phone - String contact phone number.
     * @param email - String contact email.
     * @param address - String contact address.
     * @param notes - String contact notes.
     */
    public void addContact(String name, String phone,
                           String email, String address,
                           String notes) {
        // Constructing a new ContentValues instance to hold the
        // desired row values to be added
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        values.put("email", email);
        values.put("address", address);
        values.put("notes", notes);

        // Starting up a new writeable database reference
        SQLiteDatabase db = this.getWritableDatabase();

        // Inserting the values into the table and using "" for
        // any null fields
        db.insert(TABLE_NAME, "", values);

        // Closing the database connection
        db.close();
    }


    /**
     * The getAllContacts method will search the database for all the
     * contacts in the list and return them in an ArrayList<ArrayList<String>>.
     *
     * @return - ArrayList<ArrayList<String>> of contact data.
     */
    public ArrayList<ArrayList<String>> getAllContacts() {
        // Initializing a new readable database to retrieve the contacts and
        // a temp contactList to store them
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ArrayList<String>> contactList = new ArrayList<>();

        // Using a rawQuery with cursor to retrieve all of the contact values and
        // data from the database
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // Moving the cursor to position one
        cursor.moveToFirst();

        // All the entries in the cursor will be traversed
        for (int i = 0; i < cursor.getCount(); i++) {
            // Every column in the row will be traversed
            int rowSize = cursor.getColumnCount();
            // A new temp List is made to add the contact data to
            ArrayList<String> contactRow = new ArrayList<>();

            // Getting the cursor row data and adding the row
            // list
            for (int j = 0; j < rowSize; j++) {
                contactRow.add(cursor.getString(j));
            }

            // Adding the new row to the contactList
            contactList.add(contactRow);
            // Moving the cursor to the next position for the
            // next row
            cursor.moveToNext();
        }

        // Closing cursor and database connection
        cursor.close();
        db.close();

        // Returning the contactList to the caller
        return contactList;
    }


    /**
     * The update method will update the contact row using the
     * contact id.
     *
     * @param contact - ArrayList<String> consisting of all the contact
     *                data ordered (id, name, phone, email, address, notes).
     */
    public void update(ArrayList<String> contact) {
        // Opening an instance of getWritableDatabase() referencing the
        // contactList database as this in order to update the row
        SQLiteDatabase db = this.getWritableDatabase();

        // Constructing a new ContentValues instance to hold the
        // desired row values to be updated
        ContentValues values = new ContentValues();
        values.put("name", contact.get(1));
        values.put("phone", contact.get(2));
        values.put("email", contact.get(3));
        values.put("address", contact.get(4));
        values.put("notes", contact.get(5));

        // Updating the database to the new desired entries
        db.update(TABLE_NAME, values, "id=?", new String[]{contact.get(0)});

        // Closing the database
        db.close();
    }


    /**
     * The deleteAll method will delete every contact in the database table.
     *
     */
    public void deleteAll() {
        // Initializing a new writable database instance
        SQLiteDatabase db = this.getWritableDatabase();

        // Calling the database delete method to delete all of the
        // entries in the database table by passing 'true' as the
        // whereClause
        db.delete(TABLE_NAME, "true", new String[]{});
    }
}
