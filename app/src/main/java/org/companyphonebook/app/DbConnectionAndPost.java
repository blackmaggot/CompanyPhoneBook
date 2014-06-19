package org.companyphonebook.app;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by blackmaggot on 17.06.14.
 */
public class DbConnectionAndPost {
    String host = "http://89.66.114.229/test/jsonMyql/";
    HttpClient httpclient = new DefaultHttpClient();
    HttpResponse response = null;

    public void postToDb(List inputList){
        HttpPost httpost = new HttpPost(host+"dp.php");

        try {
            httpost.setEntity(new UrlEncodedFormEntity(inputList));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {

            response = httpclient.execute(httpost);
        }
        catch (NetworkOnMainThreadException e){
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HttpResponse getFromDb(){
        String destinationAdress = host+"ReadFromDb.php";
        try {
            HttpGet request = new HttpGet(destinationAdress);
            response = httpclient.execute(request);

        } catch (IOException e){
            e.printStackTrace();
        }
        return response;
    }

    public  String streamToStringConverter(InputStream inputStream)throws IOException {
        if (inputStream !=null){
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 1024);
                int n;
                while ((n = reader.read(buffer)) != -1){
                    writer.write(buffer, 0, n);
                }
            }finally {
                inputStream.close();
            }
            return writer.toString();
        }else {
            return "";
        }
    }

    public JSONObject getStringToJson(String getRequestedString) throws JSONException {
        JSONObject jsonObject = new JSONObject(getRequestedString);
        return jsonObject;
    }
    public JSONArray getStringToJsonArray(String getRequestedString) throws JSONException{
        JSONArray jsonArray = new JSONArray(getRequestedString);
        return jsonArray;
    }
    
    public ArrayList jsonArrayToArrayList(JSONArray jsonArray) throws JSONException {
        ArrayList<Contact> Contact = new ArrayList<Contact>();
        ArrayList<String> items = new ArrayList<String>();
        for(int i=0; i< jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("ID");
            String firstName = jsonObject.getString("firstName");
            String lastName= jsonObject.getString("lastName");
            String email = jsonObject.getString("email");
            String phone = jsonObject.getString("phone");
            items.add(firstName);
            items.add(lastName);
            items.add(email);
            items.add(phone);
            Log.d(id+", "+firstName+", "+lastName+", "+email+", "+phone, "Output");
        }
        return items;
    }




}
