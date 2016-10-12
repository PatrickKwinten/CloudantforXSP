package org.quintessens.app;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import ch.belsoft.tools.XPagesUtil;

import nl.elstarit.cloudant.connector.CloudantConnector;
import nl.elstarit.cloudant.log.CloudantLogger;
import nl.elstarit.cloudant.model.ConnectorIndex;
import nl.elstarit.cloudant.model.ConnectorResponse;

import com.ibm.xsp.bluemix.util.BluemixContextUtil;

public class CloudantController {
	
	private static final String SERVICE_NAME = "cloudantNoSQLDB";
    private String account;
    private String password;
    private String username;
    private String cloudantDb;
    
    private BluemixContextUtil bluemixUtil;
    
    private XPagesUtil xpagesUtil;  
    
    private boolean connected = false;
    
    //private static final String BEAN_NAME = "CloudantService";
    
    private CloudantConnector connector;
	
	public CloudantController(){
		System.out.println("constructor cloudantcontroller");
	}

	public void hello(){
		System.out.println("hello");
	}
	
	public void connect(String cloudantDatabase){		
		if (xpagesUtil == null) {
			xpagesUtil = new XPagesUtil();
			username = xpagesUtil.getLangString("cloudant.properties", "username", "");
			password = xpagesUtil.getLangString("cloudant.properties", "password", "");
			setAccount(xpagesUtil.getLangString("cloudant.properties", "account", ""));
			cloudantDb = cloudantDatabase;
		}
		if (bluemixUtil == null) {
            bluemixUtil = new BluemixContextUtil(SERVICE_NAME, username, password, "");
        }
        
        connector = new CloudantConnector();
        connector.initCloudantClientAdvanced(bluemixUtil.getAccount(), bluemixUtil.getUsername(), bluemixUtil.getPassword(), cloudantDb, true, 1, 1, TimeUnit.MINUTES);
        
        connected = true;
	}
	
	public void connect(){
		System.out.println("connect()!");
		
		
		if (xpagesUtil == null) {
			xpagesUtil = new XPagesUtil();
			username = xpagesUtil.getLangString("cloudant.properties", "username", "");
			password = xpagesUtil.getLangString("cloudant.properties", "password", "");
			setAccount(xpagesUtil.getLangString("cloudant.properties", "account", ""));
			cloudantDb = xpagesUtil.getLangString("cloudant.properties", "database", "");;
		}
		if (bluemixUtil == null) {
            bluemixUtil = new BluemixContextUtil(SERVICE_NAME, username, password, "");
        }
        
        connector = new CloudantConnector();
        connector.initCloudantClientAdvanced(bluemixUtil.getAccount(), bluemixUtil.getUsername(), bluemixUtil.getPassword(), cloudantDb, true, 1, 1, TimeUnit.MINUTES);
        
        connected = true;
	}

	public boolean isConnected() {
		System.out.println("check isconnected");
		return connected;
	}

	public  void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	   /*
     * Database connectors
     */
    
    public void switchDatabase(String db, boolean create) {
        connector.switchDatabase(db, create);
    }
    
    public void createDatabase(String db) {
        connector.switchDatabase(db, true);
    }
    
    /*
     * Document connectors
     */
    
    public Object findDocumentByID(Class<?> cls, String documentId) {
        try {
            return connector.getDocumentConnector().find(cls, documentId);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
        return null;
    }
    
    public List<?> findAllDocuments(Class<?> cls) {
        try {
            return connector.getDocumentConnector().findAllDocuments(cls);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
        return null;
    }
    
    public void removeDocument(Object obj) {
        try {
            connector.getDocumentConnector().delete(obj);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
    }
    
    public ConnectorResponse saveDocument(Object obj) {
        ConnectorResponse resp = null;
        try {
            resp = connector.getDocumentConnector().save(obj);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
        return resp;
    }
    
    public ConnectorResponse updateDocument(Object obj) {
        ConnectorResponse resp = null;
        try {
            resp = connector.getDocumentConnector().update(obj);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
        return resp;
    }
    
    public void saveDocuments(final List<?> docs) {
        try {
            connector.getDocumentConnector().createBulk(docs);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
    }
    
    public void updateDocuments(final List<?> docs) {
        try {
            connector.getDocumentConnector().updateBulk(docs);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
    }
    
    public void deleteDocuments(final List<?> docs) {
        try {
            connector.getDocumentConnector().deleteBulk(docs);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
    }
    
    public ConnectorResponse saveStandaloneAttachment(final InputStream inputStream,
            final String name, final String contentType, final String docId,
            final String docRev) {
        try {
            return connector.getDocumentConnector().saveStandAloneAttachment(inputStream,
                    name, contentType, docId, docRev);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
        return null;
    }
    
    /**
     * 
     * @param cls
     * @param designDoc
     * @param viewName
     * @param keyType
     * @param limit
     * @return
     */
    public List<?> findAllDocumentFromView(final Class<?> cls,
            final String designDoc, final String viewName,
            final String keyType, final int limit) {
        try {
            return connector.getDocumentConnector().findAllDocumentsFromView(
                    cls, designDoc, viewName, keyType, limit);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
        return null;
    }
    
    /**
     * 
     * @param cls
     * @param designDoc
     * @param viewName
     * @param keyType
     * @param limit
     * @param startKey
     * @param endKey
     * @return
     */
    public List<?> findAllDocumentFromViewKeys(final Class<?> cls,
            final String designDoc, final String viewName,
            final String keyType, final int limit, final String startKey,
            final String endKey) {
        try {
            return connector.getDocumentConnector()
            .findAllDocumentsFromViewKeys(cls, designDoc, viewName,
                    keyType, limit, startKey, endKey);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
        return null;
    }
    
    /*
     * Query
     */
    
    public List<ConnectorIndex> allIndices() {
        return (List<ConnectorIndex>) connector.getQueryConnector()
        .allIndices();
    }
    
    public List<?> search(final String searchIndexId, final Class<?> cls,
            final Integer queryLimit, final String query) {
        
        try {
            return connector.getQueryConnector().search(searchIndexId, cls,
                    queryLimit, query);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
        return null;
    }

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccount() {
		return account;
	}
	
	
}