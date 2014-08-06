package org.exoplatform.mobile.notifications.portlet.usersettings;

import javax.inject.Inject;

import juzu.Path;
import juzu.View;
import juzu.request.RenderContext;
import juzu.template.Template;

public class UserSettings {

	@Inject
	@Path("index.gtmpl")
	Template index;
	
	@View
	public void index(RenderContext ctx)
	{
		index.render();
	}
	
}
