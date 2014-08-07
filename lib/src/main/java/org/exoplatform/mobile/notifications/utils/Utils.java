package org.exoplatform.mobile.notifications.utils;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.mobile.notifications.model.NotificationType;
import org.exoplatform.mobile.notifications.provider.MobileNotificationService;
import org.exoplatform.mobile.notifications.settings.MobileNotificationSettings;
import org.exoplatform.social.core.manager.ActivityManager;
import org.exoplatform.social.core.manager.IdentityManager;

public class Utils {

	public static boolean userHasSubscribedToNotifications(String user, int notificationType) {
		NotificationType type = NotificationType.is(notificationType);
		return getSettings().userHasSubscribedToNotificationType(type);
	}
	
	@SuppressWarnings("unchecked")
	  public static <T> T getService(Class<T> clazz) {
	    return (T) PortalContainer.getInstance().getComponentInstanceOfType(clazz);
	  }
	
	
	public static MobileNotificationService getNotificationService() {
		return getService(MobileNotificationService.class);
	}
	
	public static IdentityManager getIdentityManager() {
	    return getService(IdentityManager.class);
	  }
	
	public static String getUserId(String identityId) {
	    return getIdentityManager().getIdentity(identityId, false).getRemoteId();
	  }
	
	public static ActivityManager getActivityManager() {
	    return getService(ActivityManager.class);
	  }
	
	public static MobileNotificationSettings getSettings() {
		return getService(MobileNotificationSettings.class);
	}
}
