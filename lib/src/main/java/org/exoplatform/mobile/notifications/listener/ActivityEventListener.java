package org.exoplatform.mobile.notifications.listener;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.mobile.notifications.model.MobileNotification;
import org.exoplatform.mobile.notifications.provider.MobileNotificationService;
import org.exoplatform.social.core.activity.ActivityLifeCycleEvent;
import org.exoplatform.social.core.activity.ActivityListenerPlugin;

public class ActivityEventListener extends ActivityListenerPlugin {

	@Override
	public void likeActivity(ActivityLifeCycleEvent event) {
		
//		ExoSocialActivity activity = event.getSource();
		
		MobileNotification notif = new MobileNotification();
		notif.targetUser = "root";
		notif.eventTitle = "Someone liked your activity.";
		notif.eventMessage = "";
		
		getNotificationService().processNotification(notif);
		
	}

	@Override
	public void saveActivity(ActivityLifeCycleEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveComment(ActivityLifeCycleEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateActivity(ActivityLifeCycleEvent event) {
		// TODO Auto-generated method stub
		
	}

	private MobileNotificationService getNotificationService()
	{
		ExoContainer container = ExoContainerContext.getCurrentContainer();
		return (MobileNotificationService)container.getComponentInstanceOfType(MobileNotificationService.class);
	}

}
