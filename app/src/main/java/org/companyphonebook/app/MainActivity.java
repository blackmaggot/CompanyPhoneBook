package org.companyphonebook.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.net.Uri;


public class MainActivity extends ActionBarActivity {
    private static  final String TAG = MainActivity.class.getSimpleName();

    EditText firstName, lastName, emailAdress, phoneNumber;
    List<Contact> Contacts = new ArrayList<Contact>();
    List<CompanyContact> companyContacts = new ArrayList<CompanyContact>();
    ImageView contactImgView;
    ListView contactListView;
    ListView companyContactListView;
    Uri imageUri = null;





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
        companyContactListView = (ListView) findViewById(R.id.companyListView);
        contactImgView = (ImageView) findViewById(R.id.contactImgView);



        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("contactCreator");
        tabSpec.setContent(R.id.Creator);
        tabSpec.setIndicator("Creator");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("contactList");
        tabSpec.setContent(R.id.Contacts);
        tabSpec.setIndicator("Local Contacts");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("companyContactList");
        tabSpec.setContent(R.id.companyContacts);
        tabSpec.setIndicator("Company contacts");
        tabHost.addTab(tabSpec);
        try {
            syncCompanyContacts();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Button submitCreateButton = (Button) findViewById(R.id.submitCreateButton);
        submitCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contacts.add(new Contact(firstName.getText().toString(), lastName.getText().toString(), emailAdress.getText().toString(), phoneNumber.getText().toString(), imageUri));
                populateContactList();
                sendToDB();
                try {
                    syncCompanyContacts();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

        contactImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Contact Image"), 1);
            }
        });

    }


    public void syncCompanyContacts() throws IOException, JSONException {

        /*DbConnectionAndPost dbConnectionAndPost = new DbConnectionAndPost();
        JSONArray jsonArray = dbConnectionAndPost.getStringToJsonArray();
        for (int i = 0; i < jsonArray.length() ; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String firstName = jsonObject.getString("firstName");
            String lastName= jsonObject.getString("lastName");
            String email = jsonObject.getString("email");
            String phone = jsonObject.getString("phone");
            companyContacts.add(new CompanyContact(firstName, lastName, email, phone));
            populateCompanyContactList();
        }*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                DbConnectionAndPost dbConnectionAndPost = new DbConnectionAndPost();
                JSONArray jsonArray = null;
                try {
                    jsonArray = dbConnectionAndPost.getStringToJsonArray();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert jsonArray != null;
                for (int i = 0; i < jsonArray.length() ; i++){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = jsonArray.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    assert jsonObject != null;
                    String firstName = null;
                    try {
                        firstName = jsonObject.getString("firstName");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String lastName= null;
                    try {
                        lastName = jsonObject.getString("lastName");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String email = null;
                    try {
                        email = jsonObject.getString("email");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String phone = null;
                    try {
                        phone = jsonObject.getString("phone");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    companyContacts.add(new CompanyContact(firstName, lastName, email, phone));
                    populateCompanyContactList();
                }
            }
        }).start();
    }



    public void  onActivityResult(int reqCode, int resCode, Intent data){
        if (resCode == RESULT_OK){
            if (reqCode == 1){
                imageUri = data.getData();
                contactImgView.setImageURI(data.getData());

            }
        }
    }
    private void populateContactList(){
        ArrayAdapter<Contact> adapter = new ContactListAdapter();
        contactListView.setAdapter(adapter);
    }

    private void populateCompanyContactList(){
        ArrayAdapter<CompanyContact> adapter = new CompanyContactListAdapter();
        companyContactListView.setAdapter(adapter);
    }
    private void sendToDB() {
        try {
            final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("firstName", firstName.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("lastName", lastName.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("emailAdress", emailAdress.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("phoneNumber", phoneNumber.getText().toString()));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    DbConnectionAndPost dbConnectionAndPost = new DbConnectionAndPost();
                    dbConnectionAndPost.postToDb(nameValuePairs);

                }
            }).start();
            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    DbConnectionAndPost dbConnectionAndPost = new DbConnectionAndPost();
                    try {
                        InputStream getJson = dbConnectionAndPost.getFromDb().getEntity().getContent();
                        dbConnectionAndPost.jsonArrayToArrayList(dbConnectionAndPost.getStringToJsonArray(dbConnectionAndPost.streamToStringConverter(getJson)));
                        //Log.w("getJson", dbConnectionAndPost.streamToStringConverter(getJson));
                        //String dd = dbConnectionAndPost.getStringToJson(dbConnectionAndPost.streamToStringConverter(getJson)).getString("firstName");
                        //Log.w("getJson", dd);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
*/

        } catch (Exception e) {
            e.printStackTrace();
        }
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
            ImageView ivContactImage = (ImageView) view.findViewById(R.id.ivContactImage);
            ivContactImage.setImageURI(currentContact.get_imageURI());

            return view;

        }
    }

    private class CompanyContactListAdapter extends ArrayAdapter<CompanyContact> {
        public CompanyContactListAdapter(){
            super(MainActivity.this, R.layout.listview_item, companyContacts);
        }
        @Override
        public View getView(int position, View view, ViewGroup parent){
            if(view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            CompanyContact currentContact = companyContacts.get(position);

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
