package org.exoplatform.mobile.notifications.provider;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ValueParam;
import org.exoplatform.mobile.notifications.model.MobileNotification;
import org.exoplatform.mobile.notifications.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoogleCloudMessagingProvider implements MobileNotificationProvider {
	
	private final String PROP_API_KEY = "exo.mobile.gcm.api.key";
	private String API_KEY = "";
	
	private final String GCM_URL = "https://android.googleapis.com/gcm/send";

	public GoogleCloudMessagingProvider(InitParams params) {
		ValueParam param = params.getValueParam(Utils.MOBILE_NOTIFICATION_PROPERTIES_KEY);
		if (param != null) {
			Properties prop = Utils.loadMobileProperties(param.getValue());
			API_KEY = prop.getProperty(PROP_API_KEY);
		}
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
			jsonData.put("user", notif.targetUser);
			jsonData.put("title", notif.eventTitle);
			jsonData.put("message", notif.eventMessage);
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
		HttpClient httpClient = new DefaultHttpClient();
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
}
