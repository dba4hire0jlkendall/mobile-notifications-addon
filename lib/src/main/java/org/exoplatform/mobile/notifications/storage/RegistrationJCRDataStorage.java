package org.exoplatform.mobile.notifications.storage;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.exoplatform.mobile.notifications.model.Registration;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.ext.hierarchy.NodeHierarchyCreator;

public class RegistrationJCRDataStorage {

	private final String PARENT_NODE_LOCATION = "MobileNotificationsRegistrations";
	
	private NodeHierarchyCreator creator;
	
	public RegistrationJCRDataStorage(NodeHierarchyCreator creator, RepositoryService repositoryService)
	{
		this.creator = creator;
	}
	
	/**
	 * Adds a new device to the list of devices registered by the given user
	 * @param username The username of the user who is registering a new device
	 * @param device The details of the device to register
	 * @return true if the device was registered, false otherwise
	 */
	public boolean registerDevice(Registration reg)
	{
		if (getRegistrationsOfUsername(reg.username).contains(reg)) {
			// return true if the device is already registered
			return true;
		}
		
		Node userNode = getUserRootNode(reg.username);
		
		if (userNode != null) {
			
			try {
				Node registration = userNode.addNode(reg.device_id, StorageUtils.REGISTRATION_NODE_TYPE);
				registration.setProperty(StorageUtils.REGISTRATION_PROP_ID, reg.device_id);
				registration.setProperty(StorageUtils.REGISTRATION_PROP_PLATFORM, reg.platform.toString());
				registration.setProperty(StorageUtils.REGISTRATION_PROP_USERNAME, reg.username);
				userNode.save();
				return true; // saves the Device info in JCR and returns true
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// if we got here, there was a problem in the operations above, therefore we return false
		return false;
	}
	
	/**
	 * Retrieves the list of devices registered by the given user
	 * @param username
	 * @return An ArrayList of Device objects registered by the user, or an empty ArrayList
	 */
	public List<Registration> getRegistrationsOfUsername(String username)
	{
		ArrayList<Registration> results = new ArrayList<Registration>(5);
		Node userNode = getUserRootNode(username);
		try {
			NodeIterator it = userNode.getNodes();
			while (it.hasNext()) {
				// Each Node under the user's node represents a registered device 
				Node node = it.nextNode();
				Registration reg = new Registration(
						node.getProperty(StorageUtils.REGISTRATION_PROP_ID).getValue().getString(),
						node.getProperty(StorageUtils.REGISTRATION_PROP_PLATFORM).getValue().getString(),
						node.getProperty(StorageUtils.REGISTRATION_PROP_USERNAME).getValue().getString());
				results.add(reg);
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
			results = new ArrayList<Registration>(0); // The result ArrayList is emptied in case an error occurred in the try block
		}
		return results;
	}
	
	
	private Node getUserRootNode(String username) {
		try {
			return getProjectRootNode().getNode(username);
		} catch (PathNotFoundException e) {
			try {
				Node rootNode = getProjectRootNode();
				Node userNode = rootNode.addNode(username);
				rootNode.save();
				return userNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Node getProjectRootNode() {
	    SessionProvider sessionProvider = StorageUtils.getSystemSessionProvider();
	    try {
	      return creator.getPublicApplicationNode(sessionProvider).getNode(PARENT_NODE_LOCATION);
	    } catch (PathNotFoundException e) {
	      try {
	        Node appNode = creator.getPublicApplicationNode(sessionProvider);
	        Node ret = appNode.addNode(PARENT_NODE_LOCATION);
	        appNode.save();
	        return ret;
	      } catch(Exception ex) {
	        return null;
	      }
	    } catch (Exception e) {
	      return null;
	    }
	  }
}
