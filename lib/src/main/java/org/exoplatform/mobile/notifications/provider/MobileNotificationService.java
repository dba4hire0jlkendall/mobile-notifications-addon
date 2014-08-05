package org.exoplatform.mobile.notifications.provider;

import java.util.Iterator;
import java.util.List;
import org.exoplatform.mobile.notifications.model.MobileNotification;
import org.exoplatform.mobile.notifications.model.Registration;
import org.exoplatform.mobile.notifications.rest.RegistrationService;

public class MobileNotificationService  {
	
	private RegistrationService registrationService;
	private GoogleCloudMessagingProvider gcmProvider;
	private ApplePushNotificationProvider apnProvider;
	
	public MobileNotificationService(RegistrationService service, GoogleCloudMessagingProvider gcmProv, ApplePushNotificationProvider apnProv)
	{
		registrationService = service;
		gcmProvider = gcmProv;
		apnProvider = apnProv;
	}
	
	public void processNotification(MobileNotification notif)
	{
		List<Registration> regs = registrationService.getDevicesOfUser(notif.targetUser);
		Iterator<Registration> it = regs.iterator();
		while (it.hasNext())
		{
			Registration reg = it.next();
			if (Registration.PLATFORM_TYPE_ANDROID.equalsIgnoreCase(reg.platform))
			{
				gcmProvider.sendNotificationTo(notif, reg.device_id);
			}
			else if (Registration.PLATFORM_TYPE_IOS.equalsIgnoreCase(reg.platform))
			{
				apnProvider.sendNotificationTo(notif, reg.device_id);
			}
		}
	}
	
	
}
