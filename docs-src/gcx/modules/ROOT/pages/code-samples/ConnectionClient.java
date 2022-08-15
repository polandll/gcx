package biz.bisso.cstar;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.metadata.Metadata;
import com.datastax.oss.driver.api.core.metadata.Node;

import java.nio.file.Paths;

public class ConnectionClient {
	public static final String SECURE_CONNECT_BUNDLE = "/path/to/secure-connect-driver-demo.zip";
	public static final String CLIENT_ID = "client-id";
	public static final String CLIENT_SECRET = "client-scret";
	public static final String KEYSPACE = "demo";
	
	protected CqlSession session = null;
	
	public ConnectionClient() { }
		
	public void connect() {
        try {
        	this.session = CqlSession.builder()
    	        .withCloudSecureConnectBundle(Paths.get(SECURE_CONNECT_BUNDLE))
    	        .withAuthCredentials(CLIENT_ID, CLIENT_SECRET)
    	        .withKeyspace(KEYSPACE)
    	        .build();
        	
            Metadata metadata = this.session.getMetadata();
            for (Node node : metadata.getNodes().values()) {
            	System.out.println(node);
            }
        } catch (Exception ex) {
        	System.out.println("Error!");
        }
	}
	
	public void close() {
		this.session.close();
	}
    
	public static void main(String[] args) {
		ConnectionClient client = new ConnectionClient();
		client.connect();
		client.close();
	}
}
