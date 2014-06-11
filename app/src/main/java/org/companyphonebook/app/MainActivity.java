package org.companyphonebook.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private static  final String TAG = MainActivity.class.getSimpleName();

    EditText firstName, lastName, emailAdress, phoneNumber;
    List<Contact> Contacts = new ArrayList<Contact>();
    ListView contactListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Log.d(TAG, "onCreate() Restoring previous state");
            /* restore state */
        } else {
            Log.d(TAG, "onCreate() No saved state available");
            /* initialize app */
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        emailAdress = (EditText) findViewById(R.id.emailAdress);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        contactListView = (ListView) findViewById(R.id.listView);
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("contactCreator");
        tabSpec.setContent(R.id.Creator);
        tabSpec.setIndicator("Creator");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("contactList");
        tabSpec.setContent(R.id.Contacts);
        tabSpec.setIndicator("List");
        tabHost.addTab(tabSpec);


        final Button submitCreateButton = (Button) findViewById(R.id.submitCreateButton);
        submitCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContact(firstName.getText().toString(), lastName.getText().toString(), emailAdress.getText().toString(), phoneNumber.getText().toString());
                populateList();
                Toast.makeText((getApplicationContext()), firstName.getText().toString() + " " + lastName.getText().toString()+ "  has been saved in contacts", Toast.LENGTH_SHORT).show();
            }
        });
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                submitCreateButton.setEnabled(!firstName.getText().toString().trim().isEmpty());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void populateList(){
        ArrayAdapter<Contact> adapter = new ContactListAdapter();
        contactListView.setAdapter(adapter);
    }

    public void addContact(String firstName, String lastName, String phone, String email){
        Contacts.add(new Contact(firstName, lastName, phone, email));
    }

    private class ContactListAdapter extends ArrayAdapter<Contact> {
        public ContactListAdapter(){
            super(MainActivity.this, R.layout.listview_item, Contacts);
        }
        @Override
        public View getView(int position, View view, ViewGroup parent){
            if(view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            Contact currentContact = Contacts.get(position);

            assert view != null;
            TextView firstName = (TextView) view.findViewById(R.id.firstNameListField);
            firstName.setText(currentContact.getFirstName());
            TextView lastName = (TextView) view.findViewById(R.id.lastNameListField);
            lastName.setText(currentContact.getLastName());
            TextView email = (TextView) view.findViewById(R.id.emailListField);
            email.setText(currentContact.getEmail());
            TextView phone = (TextView) view.findViewById(R.id.phoneListField);
            phone.setText(currentContact.getPhone());

            return view;

        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
