package honyJsonParsing;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;

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
	
	public void parseFile() throws JsonParseException, IOException
	{
		JsonFactory f = new MappingJsonFactory();
		com.fasterxml.jackson.core.JsonParser jp = f.createJsonParser(jsonURL);
		JsonToken current;
		current = jp.nextToken();
		
		//see if this was valid
		if(current != JsonToken.START_OBJECT)
		{
			System.out.println("Error: root should be object!");
		}
		
		//Start searching the json file for the blog information
		while(jp.nextToken() != JsonToken.END_OBJECT)
		{
			String fieldName = jp.getCurrentName();
			current = jp.nextToken();
			
			//Find the area to start searching the file
			if(fieldName.equals("blog_name"))
			{
				while(jp.nextToken() != JsonToken.END_OBJECT)
				{
					current = jp.nextToken();
					fieldName = jp.getCurrentName();
					if(fieldName.equals("posts"))
					{
						System.out.println("progress");
					}
					else 
					{
						System.out.println("Unprocessed property: " + fieldName);
				      	jp.skipChildren();
					}
				}
				System.out.println("Success2");
			} 
			else
			{
				System.out.println("Unprocessed property: " + fieldName);
		      	jp.skipChildren();
			}
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






