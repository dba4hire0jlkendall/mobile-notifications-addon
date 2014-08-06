package org.exoplatform.mobile.notifications.listener;

import org.exoplatform.mobile.notifications.model.MobileNotification;
import org.exoplatform.mobile.notifications.model.NotificationType;
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
		String likerIdentityId = likersIdentityIds[likersIdentityIds.length - 1]; // id of the latest liker
	    String likerUser = Utils.getUserId(likerIdentityId);
	    String posterUser = Utils.getUserId(activity.getPosterId()); 
	    
	    if (!likerUser.equals(posterUser) && Utils.userHasSubscribedToNotifications(posterUser, NotificationType.LIKE_MY_ACTIVITY)) {
	    	// only send notification if the liker is not the author of the activity
		
	    	Identity likerIdentity = Utils.getIdentityManager().getIdentity(likerIdentityId, true);
	    	
			MobileNotification notif = new MobileNotification();
			notif.targetUser = posterUser;
			notif.eventTitle = likerIdentity.getProfile().getFullName()+" liked your activity";
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
		
		ExoSocialActivity comment = event.getActivity();
		ExoSocialActivity activity = Utils.getActivityManager().getParentActivity(comment);
		
		String thisCommenterId = comment.getPosterId(); // id of the commenter
		String[] allCommentersIds = activity.getCommentedIds();
		String posterId = activity.getPosterId();
		
		Identity commenterIdentity = Utils.getIdentityManager().getIdentity(thisCommenterId, true);
		
		// send to all commenters except this commenter and the author of the activity
		for (int i = 0; i < allCommentersIds.length; i++) {
			String otherCommenterId = allCommentersIds[i];
			// each value is formatted as {commenterId}@{numberOfComments} so we need to remove the @ and any number that follows
			otherCommenterId = otherCommenterId.split("@")[0];
			
			if (!otherCommenterId.equals(thisCommenterId) &&
				!otherCommenterId.equals(posterId) &&
				Utils.userHasSubscribedToNotifications(otherCommenterId, NotificationType.COMMENT_ACTIVITY_I_COMMENTED))
			{
				MobileNotification notif = new MobileNotification();
				notif.targetUser = Utils.getUserId(otherCommenterId);
				notif.eventMessage = comment.getTitle();
				notif.eventTitle = commenterIdentity.getProfile().getFullName()+" also commented on \""+activity.getTitle()+"\"";
				Utils.getNotificationService().processNotification(notif);
			}
			
			
		}
		// send to author if commenter is not the author of the activity
		if (!thisCommenterId.equals(posterId) && Utils.userHasSubscribedToNotifications(posterId, NotificationType.COMMENT_MY_ACTIVITY))
		{
			MobileNotification notif = new MobileNotification();
			notif.targetUser = Utils.getUserId(posterId);
			notif.eventTitle = commenterIdentity.getProfile().getFullName()+" commented your activity \""+activity.getTitle()+"\"";
			notif.eventMessage = comment.getTitle();
			Utils.getNotificationService().processNotification(notif);
		}
		
	}

	@Override
	public void updateActivity(ActivityLifeCycleEvent event) {
		// TODO Auto-generated method stub
		
	}

}
