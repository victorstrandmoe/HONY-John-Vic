package honyJsonParsing;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
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
	public static ArrayList<String> urlList;
	
	/**
	 * ArrayList containing the picture information for each picture
	 */
	public static ArrayList<String> captions;
	
	private String blogName;
	private URL jsonURL;
	
	private int counter = 0;
	
	public JsonParsing(URL jsonLocation, String blogNameToFind)
	{
		jsonURL = jsonLocation;
		blogName = blogNameToFind;
	}
	
	public void parseFile() throws JsonParseException, IOException //TODO, simplify and break into smaller methods
	{
		
		String fieldName;
		//Constructs reader  (Jsonparser)
		JsonFactory f = new JsonFactory();
		com.fasterxml.jackson.core.JsonParser jp = f.createJsonParser(jsonURL);
		
		JsonToken current;
		current = jp.nextToken();
		
		//Wrapper for jp to a java object with its parser set as jp
		ObjectMapper mapper = new ObjectMapper(f);
		
		
		jp.setCodec(mapper);
		
		ObjectNode node = mapper.createObjectNode();
		
		//see if this was valid
		if(current != JsonToken.START_OBJECT)
		{
			System.out.println("Error: root should be object!");
		}
		//Start searching the json file for the blog information
		try {
			while(jp.nextToken() != JsonToken.END_OBJECT)
			{
				
				fieldName = jp.getCurrentName();
				current = jp.nextToken();
				
				//Find the area to start searching the file
				if(fieldName != null && fieldName.equals("response"))
				{
					try {
						while(jp.nextToken() != JsonToken.END_OBJECT )
						{
							current = jp.nextToken();
							fieldName = jp.getCurrentName();
							if(fieldName != null && fieldName.equals("posts"))
							{

								try {
									//repeat this long horrible process 
									while(jp.nextToken() != JsonToken.END_OBJECT && counter <=10)
									{
										current = jp.nextToken();
										fieldName = jp.getCurrentName();
										
										if(fieldName != null && fieldName.equals("caption"))
										{
											//read values as branches (tree)
											node = jp.readValueAsTree(); 
											System.out.println(( "caption" + " " + node.get(fieldName)));
											System.out.println("Unprocessed property 1: " + fieldName);
											System.out.println();
											
											captions.add(node.get(fieldName).asText());
											
											do {
												current = jp.nextToken();
												fieldName= jp.getCurrentName();
											}
											while(fieldName!="image_permalink");
											node = jp.readValueAsTree();
											System.out.println(( "image_permalink" + " " + node.get(fieldName)));
											System.out.println("Unprocessed property 1: " + fieldName);
											System.out.println();
											
											urlList.add(node.get(fieldName).asText());
											
											counter++;
											System.out.println(counter);
											System.out.println("---------------------------------------------------------");
											
										}
									}
								}
								 catch (JsonProcessingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} 
							}
							else 
								
							{ 	
								try {
							      	jp.skipChildren();
								}
							 catch (JsonProcessingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
							}
						}
					}
				 catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				} 
				
				else
				{
					System.out.println("Unprocessed property 2: " + fieldName);
			      	try {
			      		//not relevent, skip eveyrthing
			      		jp.skipChildren();
					} catch (JsonParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	
	public static void main(String args[]) throws MalformedURLException, JsonParseException, IOException
	{
		urlList = new ArrayList<String>();
		captions = new ArrayList<String>();
		
		URL url = new URL("http://api.tumblr.com/v2/blog/humansofnewyork.com/posts?api_key=7ag2CJXOuxuW3vlVS5wQG6pYA6a2ZQcSCjzZsAp2pDbVwf3xEk&notes_info=true&filter=text");
		String blog = "humansofnewyork";
		
		JsonParsing parse = new JsonParsing(url, blog);
		parse.parseFile();
		
		System.out.println("--------------------------");
		System.out.println("URL: ");
		
		for(String url_temp : urlList ) {
			System.out.println(url_temp);
		}
	}
}
	






