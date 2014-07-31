package org.exoplatform.mobile.notifications.provider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.exoplatform.mobile.notifications.model.MobileNotification;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoogleCloudMessagingProvider implements MobileNotificationProvider {
	
	private String API_KEY = "";
	
	private final String GCM_URL = "https://android.googleapis.com/gcm/send";
	
	private DefaultHttpClient httpClient;

	public GoogleCloudMessagingProvider() {
		
		initProvider();
	}
	
	private void initProvider() {
		httpClient = new DefaultHttpClient();
		Properties params = loadPropertiesFromFileAtPath("/Users/philippeexo/Work/eXo/Push-Notifications-POC/GCM/poc-infos.properties");
		API_KEY = params.getProperty("poc.project.apikey");
	}
	
	private HttpPost createRequestToSendNotification(MobileNotification notif, String recipient)
	{
		HttpPost request = null;
		
		try {
			URI uri = URI.create(GCM_URL);
			
			request = new HttpPost(uri);
			request.addHeader("Authorization", "key="+API_KEY);
			request.addHeader("Content-Type", "application/json");
			
			JSONObject jsonBody = new JSONObject();
			JSONArray ids = new JSONArray();
			ids.put(recipient);
			jsonBody.put("registration_ids", ids);
			JSONObject jsonData = new JSONObject();
			jsonData.put("param", notif.eventTitle);
			jsonBody.put("data", jsonData);
			//jsonBody.put("dry_run", true);
			StringEntity body = new StringEntity(jsonBody.toString());
			body.setContentType("application/json");
			request.setEntity(body);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return request;
	}
	
	@Override
	public void sendNotificationTo(MobileNotification notif, String to) {
		
		try {
			HttpPost request = createRequestToSendNotification(notif, to);
			HttpResponse response = httpClient.execute(request);
			int responseCode = response.getStatusLine().getStatusCode();
			
			if (responseCode == HttpStatus.SC_OK) {
				handleServerResponse(); // TODO need to find way to pass response entity to the method
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

	@Override
	public void handleServerResponse() {


	}
	
	private Properties loadPropertiesFromFileAtPath(String path)
	{
			Properties prop = new Properties();
			try {
				prop.load(new FileInputStream(path));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return prop;
	}

}
