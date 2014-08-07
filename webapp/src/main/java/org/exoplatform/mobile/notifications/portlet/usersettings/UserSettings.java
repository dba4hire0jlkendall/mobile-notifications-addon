package org.exoplatform.mobile.notifications.portlet.usersettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import juzu.Path;
import juzu.Resource;
import juzu.Response;
import juzu.View;
import juzu.request.RenderContext;
import juzu.template.Template;

import org.exoplatform.commons.juzu.ajax.Ajax;
import org.exoplatform.mobile.notifications.model.NotificationType;
import org.exoplatform.mobile.notifications.settings.MobileNotificationSettings;

public class UserSettings {

	@Inject
	@Path("index.gtmpl")
	Template index;
	
	@Inject
	MobileNotificationSettings settingService;
	
	@View
	public void index(RenderContext ctx)
	{
		index.render(parameters());
	}
	
	@Ajax
	@Resource
	public Response saveSetting(String param)
	{
		try {
			NotificationType type = NotificationType.is(Integer.parseInt(param));
			
			if (settingService.userHasSubscribedToNotificationType(type)) {
				// user doesn't want to receive these notifications anymore
				settingService.removeUserSubscriptionToNotificationType(type);
			} else {
				// user wants to receive these notifications
				settingService.saveUserSubscriptionToNotificationType(type);
			}
			
		} catch (NumberFormatException e) {
			return Response.error("Not a valid notification type.");
		}
		
		return Response.ok("Saved.").withMimeType("text/plain");
	}
	
	private Map<String, Object> parameters()
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		List<NotificationType> listOfNotificationTypes = NotificationType.getAll();
		parameters.put("notificationTypes", listOfNotificationTypes);
		List<String> listOfCheckboxes = new ArrayList<String>(listOfNotificationTypes.size());
		for (NotificationType notificationType : listOfNotificationTypes) {
			listOfCheckboxes.add(notificationType.thisType(), buildCheckBox(notificationType.thisType(), settingService.userHasSubscribedToNotificationType(notificationType)));
		}
		parameters.put("checkboxes", listOfCheckboxes);
		
		return parameters;
	}
	
	private String buildCheckBox(int id, boolean isChecked) {
	    StringBuffer buffer = new StringBuffer("<span class=\"uiCheckbox\">");
	    buffer.append(("<input type=\"checkbox\" class=\"checkbox\" "))
	          .append((isChecked == true) ? "checked " : "")
	          .append("name=\"notifType").append(id).append("\" id=\"").append(id).append("\" />")
	          .append("<span></span></span>");
	    return buffer.toString();
	  }
	
}
