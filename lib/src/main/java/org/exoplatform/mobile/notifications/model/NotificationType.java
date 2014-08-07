package org.exoplatform.mobile.notifications.model;

import java.util.ArrayList;
import java.util.List;

public class NotificationType {

	public static final int LIKE_MY_ACTIVITY = 0;
	public static final int COMMENT_MY_ACTIVITY = 1;
	public static final int COMMENT_ACTIVITY_I_COMMENTED = 2;
	public static final int POST_ON_MY_STREAM = 3;
	public static final int POST_IN_MY_SPACE = 4;
	
	private int thisNotificationType;
	
	public static NotificationType is(int type)
	{
		NotificationType nt = new NotificationType();
		nt.thisNotificationType = (type>=0 && type<=4) ? type : -1;
		return nt;
	}
	
	public int thisType()
	{
		return thisNotificationType;
	}
	
	public static List<NotificationType> getAll()
	{
		ArrayList<NotificationType> list = new ArrayList<NotificationType>(5);
		list.add(NotificationType.is(NotificationType.LIKE_MY_ACTIVITY));
		list.add(NotificationType.is(NotificationType.COMMENT_MY_ACTIVITY));
		list.add(NotificationType.is(NotificationType.COMMENT_ACTIVITY_I_COMMENTED));
		list.add(NotificationType.is(NotificationType.POST_ON_MY_STREAM));
		list.add(NotificationType.is(NotificationType.POST_IN_MY_SPACE));
		return list;
	}
	
	@Override
	public String toString()
	{
		switch (thisNotificationType) {
		case LIKE_MY_ACTIVITY:
			return "Someone likes my activity";
		case COMMENT_MY_ACTIVITY:
			return "Someone comments my activity";
		case COMMENT_ACTIVITY_I_COMMENTED:
			return "Someone comments an activity I commented";
		case POST_ON_MY_STREAM:
			return "Someone posts on my personal stream";
		case POST_IN_MY_SPACE:
			return "Someone posts in a space I manage";
		default:
			return "null";
		}
	}
	
}
