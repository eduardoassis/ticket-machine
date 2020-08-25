package com.ticketmachine.cache;

public interface ICache<T> {
    void add(String key, T value);
    T get(String key);
    boolean isEmpty();
    void clear();
}