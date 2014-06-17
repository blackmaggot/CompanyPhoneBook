package org.companyphonebook.app;

import android.os.NetworkOnMainThreadException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by blackmaggot on 17.06.14.
 */
public class DbConnectionAndSending {
    String host = "http://89.66.114.229/test/jsonMyql/dp.php";
    HttpClient httpclient= new DefaultHttpClient();
    HttpPost httpost = new HttpPost(host);

    public void sendToDb(List inputList){

        try {
            httpost.setEntity(new UrlEncodedFormEntity(inputList));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {

            HttpResponse response = httpclient.execute(httpost);
        }
        catch (NetworkOnMainThreadException e){
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
