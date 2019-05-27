package project6;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.json.JSONObject;

/**
 * Hello world!
 *
 */
public class App 
{
	private static String createCQLStatement(String jsonMsg) {
		//System.out.println(jsonMsg);
		DBStorage dbs = new DBStorage();
		JSONObject json = new JSONObject(jsonMsg);
		dbs.connect("cassandra", 9042);
		dbs.insertIntoCities(json);
		dbs.close();
		
		return jsonMsg;
	}
	
    public static void main( String[] args ) throws Exception
    {
    	Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "ks-test-cities");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        
        
        final StreamsBuilder builder = new StreamsBuilder();
        builder.<String, String>stream("test-meetup-cities")
        	.flatMapValues(value -> Arrays.asList(createCQLStatement(value)));

        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, props);
        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
        	@Override
        	public void run() {
        		streams.close();
        		latch.countDown();
        	}
        });

		 try {
		     streams.start();
		     latch.await();
		 } catch (Throwable e) {
		     System.exit(1);
		 }
		 System.exit(0);
    }
}
