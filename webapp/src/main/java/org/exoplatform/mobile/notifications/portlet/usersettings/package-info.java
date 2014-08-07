@Application
@Portlet(name="UserSettingsPortlet")
@Bindings({
    @Binding(MobileNotificationSettings.class)
  }
)
@Assets
package org.exoplatform.mobile.notifications.portlet.usersettings;

import juzu.Application;
import juzu.plugin.asset.Assets;
import juzu.plugin.binding.Binding;
import juzu.plugin.binding.Bindings;
import juzu.plugin.portlet.Portlet;

import org.exoplatform.mobile.notifications.settings.MobileNotificationSettings;

