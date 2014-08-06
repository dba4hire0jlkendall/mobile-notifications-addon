package org.exoplatform.mobile.notifications.utils;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.mobile.notifications.provider.MobileNotificationService;
import org.exoplatform.social.core.manager.ActivityManager;
import org.exoplatform.social.core.manager.IdentityManager;

public class Utils {

	public static boolean userHasSubscribedToNotifications(String user, int notificationType) {
		// TODO Check here if user has subscribed to notificationType in his settings
		return true;
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
}
