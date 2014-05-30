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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonParsing {
	 private static final String layer1 = "response";
	 private static final String layer2 = "posts";
	 private static final String layer3 = "photos";
	 //private static final String layer4 = "photos";
	 private static final String dataField1 = "caption";
	 private static final String dataField2 = "url";
	
	private URL jsonURL;
	private int counter;
	private String fieldName;
	private JsonFactory factory;
	private ObjectMapper mapper;
	private ObjectNode node;
	private JsonToken current;
	private com.fasterxml.jackson.core.JsonParser jsonParser;
	
	
	
	
	public JsonParsing(URL jsonLocation)
	{
		jsonURL = jsonLocation;
	}
	
	public void parseFile(int layers, ArrayList<String> data) throws JsonParseException, IOException //TODO, simplify and break into smaller methods
	{
		fieldName = null;
		
		factory = new JsonFactory();
		jsonParser = factory.createJsonParser(jsonURL);
		
		//Wrapper for jp to a java object with its parser set as jp
		mapper = new ObjectMapper(factory);
		jsonParser.setCodec(mapper);
		node = mapper.createObjectNode();
		
		JsonToken current;
		current = jsonParser.nextToken();
		
		//see jsonToken registers as valid
		if(current != JsonToken.START_OBJECT)
		{
			System.out.println("Error: root should be object!");
		}
		
		counter = 0;
		//begin the parsing!
		while(jsonParser.nextToken() != JsonToken.END_OBJECT)
		{
			goLayer(layers);
			if(counter == layers)
			{
				System.out.println("1");
				String info = getData(dataField2);
				System.out.println(info);
				data.add(info);
				
				counter = 2;
			}
		}		
	}
	
	private void goLayer(int layers) throws JsonParseException, IOException
	{
		String fieldToFind = setLayerString(counter + 1);
		
		while(jsonParser.nextToken() != JsonToken.END_OBJECT)
		{

			fieldName = jsonParser.getCurrentName();
			current = jsonParser.nextToken();

			//Find the area to start searching the file
			if(fieldName != null && fieldName.equals(fieldToFind))
			{
				counter += 1;
				return;
			}
			else 
			{
				jsonParser.skipChildren();
				System.out.println("Unprossed property while " + counter + " layers deep. Property: " + fieldName);
			}
		}		
	}
	
	private String getData(String dataName) throws JsonParseException, IOException
	{
		while(fieldName!= dataName)
		{
			current = jsonParser.nextToken();
			fieldName= jsonParser.getCurrentName();
		}
		node = jsonParser.readValueAsTree();
		return node.get(dataName).asText();
	}
	
	private String setLayerString(int layer)
	{
		switch(layer)
		{
		case 1: return layer1;
		case 2: return layer2;
		case 3: return layer3;
		//case 4: return layer4;
		default: return layer1;
		}
	}
	
	public static void main(String args[]) throws MalformedURLException, JsonParseException, IOException
	{
		
		URL url = new URL("http://api.tumblr.com/v2/blog/humansofnewyork.com/posts?api_key=7ag2CJXOuxuW3vlVS5wQG6pYA6a2ZQcSCjzZsAp2pDbVwf3xEk&notes_info=true&filter=text");
		
		JsonParsing parse = new JsonParsing(url);
		
		ArrayList<String> urls = new ArrayList<String>();
		parse.parseFile(3, urls);
		
		System.out.println(urls.size());
	}
}
	






