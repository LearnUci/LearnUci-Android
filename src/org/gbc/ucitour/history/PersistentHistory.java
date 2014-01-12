package org.gbc.ucitour.history;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.gbc.ucitour.model.History;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.os.Environment;

/**
 * Reads from and writes to a persistent storage. Manages the history. Can read snapshot histories
 * and write whenever adding a new history element.
 */
public class PersistentHistory {
  private static PersistentHistory instance;
  private List<History> history = new ArrayList<History>();
  private static final String DIR = "LearnUci";
  private static final String FILE = "history.luci";
  
  public static final String TYPE_SEARCH = "Search";
  public static final String TYPE_TOUR = "Tour";
  
  private PersistentHistory() {
    // Get the folder and file, creating any directories that don't exist
    File root = Environment.getExternalStorageDirectory();
    File dir = new File(root, DIR);
    if (!dir.exists()) {
      dir.mkdirs(); 
    }
    
    File file = new File(root, DIR + "/" + FILE);
    
    // If the file exists, load the history. It is a json array.
    if (file.exists()) {
      try {
        BufferedReader br = new BufferedReader(new FileReader(file));
        JsonParser parser = new JsonParser();
        JsonArray arr = (JsonArray) parser.parse(br.readLine());
        for (JsonElement element : arr) {
          JsonObject obj = (JsonObject) element;
          // For each json object in the array, convert it into a history object. Note that the
          // history list is in reverse orde
          // (latest history object is at the list and is displayed first)
          history.add(new History(obj));
        }
        br.close();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  /**
   * Singleton instance
   * @return The singleton instance
   */
  public static PersistentHistory getInstance() {
    if (instance == null) {
      instance = new PersistentHistory();
    }
    return instance;
  }
  
  /**
   * Add a history element
   * @param type The type of history element
   * @param keyword The keyword
   * @param id The id associated with the type
   * @param timestamp The timestamp
   */
  public void addHistory(String type, String keyword, String id, String timestamp) {
    history.add(new History(type, keyword, id, timestamp));
    // Only keep track of the latest 100 elements
    if (history.size() > 100) {
      history.remove(0);
    }
    
    // Write the history elements back into the file as a json array
    JsonArray arr = new JsonArray();
    for (History h : history) {
      arr.add(h.json());
    }
    try {
      File root = Environment.getExternalStorageDirectory();
      File file = new File(root, DIR + "/" + FILE);
      PrintWriter pw = new PrintWriter(file);
      pw.println(arr.toString());
      pw.flush();
      pw.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Snapshot the current history.
   * @return The snapshotted current history, returned in reverse order so the first element is the
   *         most recent and is displayed first.
   */
  public History[] listHistory() {
    History[] historyArr = new History[history.size()];
    for (int i = 0; i < historyArr.length; i++) {
      historyArr[i] = history.get(history.size() - 1 - i); 
    }
    return historyArr;
  }
}
