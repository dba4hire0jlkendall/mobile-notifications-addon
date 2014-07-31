package org.exoplatform.notifications.rest.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.exoplatform.notifications.model.Registration;
import org.exoplatform.notifications.storage.RegistrationJCRDataStorage;
import org.exoplatform.notifications.storage.StorageUtils;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/exo-mobile-notifications")
public class RegistrationService implements ResourceContainer  {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/registrations")
	public Response registerDeviceOfUser(@Context SecurityContext sc, Registration registration)
	{
		JSONObject jsonReg = new JSONObject();
		CacheControl cacheControl = new CacheControl();
		cacheControl.setNoCache(true);
	    cacheControl.setNoStore(true);
	    
//	    TODO implement authentication for this service
//		Principal user = sc.getUserPrincipal();
//	    if (user == null || !isInPlatformUsersGroup(user.getName()))
//	    {
//	    	return Response.status(Status.FORBIDDEN).build();
//	    }
//	    else
	    {
	    	try {
	    		
	    		if (StorageUtils.getService(RegistrationJCRDataStorage.class).registerDevice(registration))
	    		{
					jsonReg.put("device_id", registration.id);
					jsonReg.put("platform" , registration.platform);
					jsonReg.put("username" , registration.username);
	    		}
	    		else
	    		{
	    			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Registration failed.").build();
	    		}
				
				
			} catch (JSONException e) {
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Device registered successfully but cannot create response.").build();
			}
	    }
	    
	    return Response.ok(jsonReg.toString(), MediaType.APPLICATION_JSON).build();
	}
	
	public List<Registration> getDevicesOfUser(String username) {
		return StorageUtils.getService(RegistrationJCRDataStorage.class).getRegistrationsOfUsername(username);
	}
	
//	private boolean isInPlatformUsersGroup(String username) {
//	    ExoContainer container = ExoContainerContext.getCurrentContainer();
//	    IdentityRegistry identityRegistry = (IdentityRegistry) container.getComponentInstanceOfType(IdentityRegistry.class);
//	    Identity identity = identityRegistry.getIdentity(username);
//	    return identity.isMemberOf("/platform/users");
//	  }
	
}
