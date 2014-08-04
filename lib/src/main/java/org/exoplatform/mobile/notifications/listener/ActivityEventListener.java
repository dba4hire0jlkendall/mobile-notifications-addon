package org.exoplatform.mobile.notifications.listener;

import org.exoplatform.mobile.notifications.model.MobileNotification;
import org.exoplatform.mobile.notifications.utils.Utils;
import org.exoplatform.social.core.activity.ActivityLifeCycleEvent;
import org.exoplatform.social.core.activity.ActivityListenerPlugin;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.identity.model.Identity;

public class ActivityEventListener extends ActivityListenerPlugin {

	@Override
	public void likeActivity(ActivityLifeCycleEvent event) {
		
		ExoSocialActivity activity = event.getActivity();
		
		String[] likersIdentityIds = activity.getLikeIdentityIds();
		String likerIdentityId = likersIdentityIds[likersIdentityIds.length - 1];
	    String likerUser = Utils.getUserId(likerIdentityId);
	    String posterUser = Utils.getUserId(activity.getPosterId()); 
	    
	    if (!likerUser.equals(posterUser)) { // only send notification if liker != poster
		
	    	Identity likerIdentity = Utils.getIdentityManager().getIdentity(likerIdentityId, true);
	    	
			MobileNotification notif = new MobileNotification();
			notif.targetUser = posterUser;
			notif.eventTitle = likerIdentity.getProfile().getFullName()+" liked your activity.";
			notif.eventMessage = activity.getTitle();
			
			Utils.getNotificationService().processNotification(notif);
	    }
		
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

}
