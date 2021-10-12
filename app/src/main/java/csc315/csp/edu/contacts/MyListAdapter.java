/* Devin Brueberg
 * CSC 315 Programming Assignment 6
 * MyListAdapter.java
 * October 10, 2021
 * Updated(Initials, Date, Changes):
 *  (DAB, 10/11/2021, Added Comments)
 *
 */
// Package of the program
package csc315.csp.edu.contacts;

// Importing needed android/java modules
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * The MyListAdapter is a custom list display that will use
 * an ArrayList of ArrayList<String> in order to display the
 * name and phone number of contacts in the list.
 *
 * As of 10/11/2021 the layout consists of a horizontal view with
 * two text views and an image on the far right.
 */
public class MyListAdapter extends BaseAdapter {
    // Declaring the context and list that will be used to populate the rows
    private Context context;
    private final ArrayList<ArrayList<String>> contactList;

    /**
     * Constructor that will initialize the list adapter object with the proper
     * data list and context.
     *
     * @param context - Context of the adapter.
     * @param contactList - ArrayList<ArrayList<String>> contact data.
     */
    public MyListAdapter(@NonNull Context context, @NonNull ArrayList<ArrayList<String>> contactList) {
        // Assigning the correct references to the class variables
        this.contactList = contactList;
        this.context = context;
    }


    /**
     * Returns the count of the list.
     *
     * @return - int list size.
     */
    @Override
    public int getCount() {
        return contactList.size();
    }


    /**
     * Returns the item at the specified int location.
     *
     * @param position - int index location.
     * @return - ArrayList<String> consisting of a single contact.
     */
    @Override
    public ArrayList<String> getItem(int position) {
        return contactList.get(position);
    }


    /**
     * Returns the int position in the contact in the list.
     *
     * @param position - int index of contact.
     * @return - the int index of contact.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * The getView creates and displays the custom view. It pieces them together
     * one at a time using the contact position to retrive and display the
     * data as appropriate. A custom class named viewHolder is used for quick
     * reference to the row views.
     *
     * @param position - int index of contact in the list.
     * @param convertView - The current created view.
     * @param parent - The parent view that will contain the convertView.
     * @return - The current view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Declaring a viewHolder instance
        ViewHolder viewHolder;

        // If this is the first time using the view a new view is created and the viewHolder variable
        // is constructed
        if (convertView == null) {
            // Creating the view and assigning viewHolder to stick to it
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_list_display, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            // The view is already active so viewHolder is assigned to it
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Retrieving the current contact
        ArrayList<String> contact = getItem(position);

        // If the contact is not null, it is set to the textViews in the
        // custom row view
        if (viewHolder.nameView != null) {
            viewHolder.nameView.setText(contact.get(1));
        }
        if (viewHolder.phoneView != null) {
            viewHolder.phoneView.setText(contact.get(2));
        }

        // Returning the assembled row to the view
        return convertView;
    }

    /**
     * The ViewHolder class holds id references to the views in the custom
     * row layout. It is used so they do not need to be retrieved for every
     * row view construction.
     */
    private class ViewHolder {
        // Declaring the class specific view reference variables
        TextView nameView;
        TextView phoneView;

        // Constructing a ViewHolder object with id references to the custom
        // row view
        public ViewHolder(View view) {
            nameView = view.findViewById(R.id.displayNameTextView);
            phoneView = view.findViewById(R.id.displayPhoneTextView);
        }
    }
}
