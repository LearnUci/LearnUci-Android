package org.learnuci.net;

import java.util.concurrent.Semaphore;

/**
 * A container class that holds a value that may not necessarily exist yet.
 * Blocks until the value is ready only when trying to get it.
 * @param <T> The type of value
 */
public class Future<T> {
  private boolean hasSet = false;
  private Semaphore semaphore;
  private T value;
  
  Future() {
    this.semaphore = new Semaphore(0);
  }
  
  void set(T value) {
    this.value = value;
    this.semaphore.release();
    this.hasSet = true;
  }
  
  /**
   * Blocks until the value is ready
   * @return The value that this holds
   */
  public T get() {
    if (!hasSet) {
      try {
        semaphore.acquire();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    return value;
  }
  
  /**
   * @return True if value has been set.
   */
  public boolean hasSet() {
    return hasSet;
  }
}
