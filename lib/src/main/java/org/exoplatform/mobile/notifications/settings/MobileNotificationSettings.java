package org.exoplatform.mobile.notifications.settings;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.commons.api.settings.SettingService;
import org.exoplatform.commons.api.settings.SettingValue;
import org.exoplatform.commons.api.settings.data.Context;
import org.exoplatform.commons.api.settings.data.Scope;
import org.exoplatform.mobile.notifications.model.NotificationType;

public class MobileNotificationSettings {
	
	private final String KEY_PREFIX = "notifType";

	private SettingService settingService;
	
	public MobileNotificationSettings(SettingService service)
	{
		settingService = service;
	}
	
	private String keyOfType(NotificationType type)
	{
		return KEY_PREFIX+"_"+type.thisType();
	}
	
	public void saveUserSubscriptionToNotificationType(NotificationType type)
	{
		SettingValue<String> value = SettingValue.create(""+type);
		settingService.set(Context.USER, Scope.GLOBAL, keyOfType(type), value);
	}
	
	public void removeUserSubscriptionToNotificationType(NotificationType type)
	{
		settingService.remove(Context.USER, Scope.GLOBAL, keyOfType(type));
	}
	
	public boolean userHasSubscribedToNotificationType(NotificationType type)
	{
		@SuppressWarnings("unchecked")
		SettingValue<String> value = (SettingValue<String>)settingService.get(Context.USER, Scope.GLOBAL, keyOfType(type));
		return value != null; // true if a value exists, false if no value was found
	}
	
	public List<NotificationType> getAllSubscriptionsOfUser()
	{
		ArrayList<NotificationType> subscriptions = new ArrayList<NotificationType>(10);
		List<NotificationType> all = NotificationType.getAll();
		
		for (NotificationType notificationType : all) {
			if (userHasSubscribedToNotificationType(notificationType)) {
				subscriptions.add(notificationType);
			}
		}
		
		return subscriptions;
	}
}
