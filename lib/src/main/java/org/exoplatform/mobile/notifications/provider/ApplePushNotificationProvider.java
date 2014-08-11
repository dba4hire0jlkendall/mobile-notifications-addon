package org.exoplatform.mobile.notifications.provider;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Properties;

import javax.net.ssl.SSLHandshakeException;

import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ValueParam;
import org.exoplatform.mobile.notifications.model.MobileNotification;
import org.exoplatform.mobile.notifications.utils.Utils;

import com.relayrides.pushy.apns.ApnsEnvironment;
import com.relayrides.pushy.apns.ExpiredToken;
import com.relayrides.pushy.apns.FailedConnectionListener;
import com.relayrides.pushy.apns.FeedbackConnectionException;
import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.PushManagerFactory;
import com.relayrides.pushy.apns.RejectedNotificationListener;
import com.relayrides.pushy.apns.RejectedNotificationReason;
import com.relayrides.pushy.apns.util.ApnsPayloadBuilder;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import com.relayrides.pushy.apns.util.TokenUtil;

public class ApplePushNotificationProvider implements MobileNotificationProvider,
                                                      RejectedNotificationListener<SimpleApnsPushNotification>,
                                                      FailedConnectionListener<SimpleApnsPushNotification>
{
	private PushManager<SimpleApnsPushNotification> pushManager = null;
	private final String CERTIFICATE_FILE_PATH = "exo.mobile.apn.certificate.path";
	private final String CERTIFICATE_PASSWORD  = "exo.mobile.apn.certificate.password";
	private String pathToCertificate;
	private String certificatePassword;
	
	public ApplePushNotificationProvider(InitParams params) {
		ValueParam param = params.getValueParam(Utils.MOBILE_NOTIFICATION_PROPERTIES_KEY);
		if (param != null) {
			String pathToMobileProperties = param.getValue();
			Properties mobileProperties = Utils.loadMobileProperties(pathToMobileProperties);
			pathToCertificate = mobileProperties.getProperty(CERTIFICATE_FILE_PATH);
			certificatePassword = mobileProperties.getProperty(CERTIFICATE_PASSWORD);
			initProvider();
		}
	}
	
	private void initProvider() {
		createPushManager();
		registerErrorListeners();
	}
	
	public void createPushManager()
	{
		PushManagerFactory<SimpleApnsPushNotification> pushManagerFactory = null;
		
		try {
			pushManagerFactory = new PushManagerFactory<SimpleApnsPushNotification>(
			        ApnsEnvironment.getSandboxEnvironment(),
			        PushManagerFactory.createDefaultSSLContext(pathToCertificate, certificatePassword));
			
			pushManager = pushManagerFactory.buildPushManager();
			pushManager.start();
			
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void registerErrorListeners()
	{
		if (pushManager != null && pushManager.isStarted()) {
			pushManager.registerRejectedNotificationListener(this);
			pushManager.registerFailedConnectionListener(this);
		}
	}
	
	@Override
	public void sendNotificationTo(MobileNotification notif, String to) {
		if (pushManager != null) {
			final byte[] token = TokenUtil.tokenStringToByteArray(to);
			final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
	//		payloadBuilder.setAlertBody(notif.eventTitle);
			 payloadBuilder.addCustomProperty("user", notif.targetUser);
			 payloadBuilder.addCustomProperty("title", notif.eventTitle);
			 payloadBuilder.addCustomProperty("message", notif.eventMessage);
			 payloadBuilder.setContentAvailable(true);
			
			final String payload = payloadBuilder.buildWithDefaultMaximumLength();
	
			try {
				pushManager.getQueue().put(new SimpleApnsPushNotification(token, payload));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void handleServerResponse() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleFailedConnection(PushManager<? extends SimpleApnsPushNotification> mgr, Throwable cause) {
		
		if (cause instanceof SSLHandshakeException) {
            // This is probably a permanent failure, and we should shut down
            // the PushManager.
        }
	}

	@Override
	public void handleRejectedNotification(PushManager<? extends SimpleApnsPushNotification> mgr,
			                               SimpleApnsPushNotification notif, RejectedNotificationReason reason) {
		
	}
	
	private void retrieveDisabledDevices()
	{
		try {
			for (final ExpiredToken token : pushManager.getExpiredTokens()) {
			    // TODO: Stop sending push notifications to each expired token if the expiration
			    // time is after the last time the app registered that token.
				// unregister said token from the RegistrationService
				String tokenString = TokenUtil.tokenBytesToString(token.getToken());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (FeedbackConnectionException e) {
			e.printStackTrace();
		}
	}

}
