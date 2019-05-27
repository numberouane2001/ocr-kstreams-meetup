package project6;

import org.json.JSONObject;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;

public class DBStorage {

	private Cluster cluster;
    private Session session;
    private String node;
    private Integer port;
    
    public void connect(String node, Integer port) {
    	this.node = node;
    	this.port = port;
        Builder b = Cluster.builder().addContactPoint(node);
        if (port != null) {
            b.withPort(port);
        }
        cluster = b.build();
 
        session = cluster.connect();
    }
 
    public Session getSession() {
        return this.session;
    }
 
    public void close() {
        session.close();
        cluster.close();
    }
    
    public void insertIntoEvents(JSONObject json)
    {
    	System.out.println(json.has("venue"));
    	if (json.has("venue")==true)
    	{
	    	JSONObject city = json.getJSONObject("venue");
	    	StringBuilder sb = new StringBuilder("");
	    	//eventid | cityid | country | eventdate | eventname | lat | lon | status
	    	sb= sb.append("UPDATE test_meetup.events SET ")
	    			.append("cityid = ").append(city.getInt("id"))
	    			.append(", country = '").append(city.getString("country")).append("'")
	    			.append(", eventdate = ").append(json.getLong("time"))
	    			.append(", eventname = $$").append(json.getString("name")).append("$$")
	    			.append(", lat = ").append(city.getFloat("lat"))
	    			.append(", lon = ").append(city.getFloat("lon"))
	    			.append(", status = '").append(json.getString("status")).append("'")
	    			.append(" WHERE eventid = '").append(json.getString("id")).append("';");
	    	
	    	String query = sb.toString();
	        session.execute(query);
	    	
    	}
    	else {
    		System.out.println("No city defined.");
    		System.out.println(json);
    	}
    }
    
    public void insertIntoCities(JSONObject json)
    {
    	StringBuilder sb = new StringBuilder("");
    	//cityid | address | city | country | fullcityname | lat | lon | members | state | zipcode
    	sb= sb.append("UPDATE test_meetup.cities SET ")
    			.append(" country = $$").append(json.getString("country")).append("$$ ")
    			.append(", lat = ").append(json.getFloat("lat"))
    			.append(", lon = ").append(json.getFloat("lon"))
    			.append(", city = $$").append(json.getString("city")).append("$$ ")
    			.append(", members = ").append(json.getInt("member_count"))
    			.append(" WHERE cityid = ").append(json.getInt("id")).append(";");
    			
    	String query = sb.toString();
        session.execute(query);
    }
	
}
