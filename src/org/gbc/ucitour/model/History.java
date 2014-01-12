package org.gbc.ucitour.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Data object for a history element
 */
public class History {
  public String type;
  public String keyword;
  public String id;
  public String timestamp;
  
  @Override
  public String toString() {
    return type + ": " + keyword;
  }
  
  public JsonObject json() {
    JsonObject obj = new JsonObject();
    obj.add("type", new JsonPrimitive(type));
    obj.add("keyword", new JsonPrimitive(keyword));
    obj.add("id", new JsonPrimitive(id));
    obj.add("timestamp", new JsonPrimitive(timestamp));
    return obj;
  }
  
  public History(JsonObject obj) {
    type = obj.get("type").getAsString();
    keyword = obj.get("keyword").getAsString();
    id = obj.get("id").getAsString();
    timestamp = obj.get("timestamp").getAsString();
  }
  
  public History(String type, String keyword, String id, String timestamp) {
    this.type = type;
    this.keyword = keyword;
    this.id = id;
    this.timestamp = timestamp;
  }
}
