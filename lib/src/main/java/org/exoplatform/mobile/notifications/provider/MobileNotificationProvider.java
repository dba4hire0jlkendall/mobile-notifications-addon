package org.exoplatform.mobile.notifications.provider;

import org.exoplatform.mobile.notifications.model.MobileNotification;

public interface MobileNotificationProvider {

	
	public void sendNotificationTo(MobileNotification notif, String to);
	
	public void handleServerResponse();
	

}
