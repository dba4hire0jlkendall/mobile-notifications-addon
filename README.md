mobile-notifications-addon
==========================


This addon allows Platform to send push notifications to users' mobile devices. It is just a POC, therefore there are few needed improvements listed at the end before it can be used in production.

The addon is composed of:

* a library (module lib) that contains a few eXo services
* an extension (modules config and webapp) that contains a portlet and configuration for a new page

Library
=======

The library contains these services:

* a Rest service RegistrationService to register/unregister a mobile device in Platform
* RegistrationJCRDataStorage that connects to JCR services to persist the registrations info
* GoogleCloudMessagingProvider to send notifications to Android devices
* ApplePushNotificationProvider to send notifications to iOS devices
* MobileNotificationService that dispatches notifications to the proper provider (cf above)
* MobileNotificationSettings that connects to the Platform's SettingService to store users' settings

The library also contains an event listener to catch when certain events happen in Platform and eventually trigger a notification for them.
Currently, only these events trigger notifications:

* ActivityEventListener.likeActivity

  When a user likes an activity, the activity's author may be notified

* ActivityEventListener.saveComment

  When a user comments an activity, the activity's author and other commenters may be notified


These services and listeners all work together to implement the logic and workflow of the mobile notifications.
The workflow is as follows:

1. A user connects with a mobile device to his account in Platform,
1. Upon successful login, his device ID, username and device's platform (Android or iOS) are registered with the RegistrationService,
1. When an event is caught by the event listener, the listener uses the MobileNotificationSettings service to check whether the potential recipient has subscribed to notifications for this event,
1. If yes, a notification is created and passed to the MobileNotificationService,
1. The MobileNotificationService has only one role: dispatch the notification to all devices registered for the recipient user,
1. If the user has an Android device registered, the notification is passed to the GoogleCloudMessagingProvider service,
1. If the user has an iOS device registered, the notification is passed to the ApplePushNotificationProvider service,
1. Each provider connects with the corresponding service from Google or Apple to send the notification to the device


Extension
=========

The extension contains a portlet UserSettingsPortlet that is configured on a page accessible from the left navigation menu in Platform.
This portlet lists all types of events that can trigger a notification. For each event, the user can choose to receive a notification or not by activating a checkbox or not.
The portlet is developed in Juzu and uses Ajax to immediately save the user's choice when he clicks on a checkbox. The saving status is indicated by a label below the list of events that shows one of the three states: saved, saving or error.

Each time the user clicks on a checkbox, the portlet calls the MobileNotificationSettings service to store the user's choice, i.e. whether they want to receive mobile notifications for this type of event or not.

Installation
============

To install this addon on your instance on Platform, follow these steps:

1. Use the addons-manager to download the addon bundle and extract it into Platform

   `./addon install --snapshots exo-mobile-notifications`

1. Create a properties file to authenticate to Google service

   * Add a property with key `exo.mobile.gcm.api.key` and your actual API key as a value
   * [Google's reference](http://developer.android.com/google/gcm/gs.html#access-key)

1. Configure some properties to authenticate to Apple service

   * Install a p12 certificate on your filesystem
   * In the properties file created above, add a property with key `exo.mobile.apn.certificate.path` and the path to the p12 file as value
   * Then create another property with key `exo.mobile.apn.certificate.password` and the certificate password as value
   * [Apple's reference](https://developer.apple.com/library/ios/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/Chapters/ProvisioningDevelopment.html#//apple_ref/doc/uid/TP40008194-CH104-SW1)

1. In `bin/setenv-customize.sh` add a system variable as follows

   `CATALINA_OPTS="${CATALINA_OPTS} -Dexo.mobile.notifications.properties.file=/path/to/mobile-notifications.properties"`

The last 3 steps are necessary only to connect to the Google and Apple service to actually send the notifications. However, you can still start Platform and see the user settings portlet without this configuration.

TODO
====

This paragraph lists a few improvements needed to make this addon ready for production use, most important first:

* Error management

  Google and Apple provide few types of feedback when notifications are sent. For example:

  * Whether a given device ID is invalid. It can happen when a user uninstalls the application from their device,
  * The Apple service informs when the certificate used for authentication is about to expire,

  The services in the addon's library must take this feedback into account.

* Performance improvements

  Multi-threading and queueing techniques can be used to keep the performances impact at a minimum on Platform. 
  Besides, the use of the Google and Apple services can be improved. For ex, Google's service allows to send a notification to up to 1000 recipients in a single request.

* Plugin mechanism to extend the types of event that trigger mobile notifications

  Like what was done in Social for Email Notifications, this system should allow 3rd party developers to plug in their own listeners.

* I18n and L10n

  All labels are hardcoded and in English only. A production-ready addon must ensure that labels are displayed in the language of the user.

