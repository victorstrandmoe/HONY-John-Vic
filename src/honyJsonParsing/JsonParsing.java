
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonParsing {
	/**
	 * ArrayList containing the list of urls of the pictures
	 */
	private ArrayList<URL> urlList;
	
	/**
	 * ArrayList containing the picture information for each picture
	 */
	private ArrayList<String> captions;
	
	private String blogName;
	private URL jsonURL;
	
	
	
	public JsonParsing(URL jsonLocation, String blogNameToFind)
	{
		jsonURL = jsonLocation;
		blogName = blogNameToFind;
	}
	
	public void parseFile() throws JsonParseException, IOException //TODO, simplify and break into smaller methods
	{
		
		//Constructs reader  (Jsonparser)
		JsonFactory f = new JsonFactory();
		com.fasterxml.jackson.core.JsonParser jp = f.createJsonParser(jsonURL);
		
		JsonToken current;
		current = jp.nextToken();
		
		//Wrapper for jp to a java object with its parser set as jp
		ObjectMapper mapper = new ObjectMapper(f);
		
		
		jp.setCodec(mapper);
		
		ObjectNode node = mapper.createObjectNode();
		
		int counter1 = 0;
		//see if this was valid
		if(current != JsonToken.START_OBJECT)
		{
			System.out.println("Error: root should be object!");
		}
		
		//Start searching the json file for the blog information
		while(jp.nextToken() != JsonToken.END_OBJECT && counter1 <20)
		{
			String fieldName = jp.getCurrentName();
			current = jp.nextToken();
			
			//Find the area to start searching the file
			if(fieldName != null && fieldName.equals("response"))
			{
				while(jp.nextToken() != JsonToken.END_OBJECT)
				{
					current = jp.nextToken();
					fieldName = jp.getCurrentName();
					if(fieldName != null && fieldName.equals("posts"))
					{
						System.out.println("progress");
						
						//repeat this long horrible process 
						while(jp.nextToken() != JsonToken.END_OBJECT)
						{
							current = jp.nextToken();
							fieldName = jp.getCurrentName();
							
							if(fieldName != null && fieldName.equals("caption"))
							{
								node = jp.readValueAsTree(); 

								System.out.println(( "Caption: " + node.get(fieldName)));
								System.out.println("Unprocessed property 1: " + fieldName);
								System.out.println();
								System.out.println("------------------------------------------------------------------------");
								
							}
							
							current = jp.nextToken();
							fieldName = jp.getCurrentName();
							if(fieldName != null && fieldName.equals("caption"))
							{
								node = jp.readValueAsTree(); 

								System.out.println(( "Caption: " + node.get(fieldName)));
								System.out.println("Unprocessed property 1: " + fieldName);
								System.out.println();
								System.out.println("------------------------------------------------------------------------");
								
							}
						}
					}
					else 
						
					{ 	
						
						//the node can now view the  parser as a tree and get values
						node = jp.readValueAsTree(); 
						
						System.out.println(( node.get(fieldName) + " URL"));
						System.out.println("Unprocessed property 2: " + fieldName);
				      	jp.skipChildren();
					}
				}
			} 
			else
			{
				System.out.println("Unprocessed property 2: " + fieldName);
		      	jp.skipChildren();
			}
			counter1++;
		}
	}
	
	
	public static void main(String args[]) throws MalformedURLException, JsonParseException, IOException
	{
		URL url = new URL("http://api.tumblr.com/v2/blog/humansofnewyork.com/posts?api_key=7ag2CJXOuxuW3vlVS5wQG6pYA6a2ZQcSCjzZsAp2pDbVwf3xEk&notes_info=true&filter=text");
		String blog = "humansofnewyork";
		
		JsonParsing parse = new JsonParsing(url, blog);
		parse.parseFile();
	}
	
}






