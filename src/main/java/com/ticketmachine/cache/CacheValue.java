package com.ticketmachine.cache;

public class CacheValue<T> {
    private T value;
    private long expiryTime;

    public CacheValue(T value, long expiryTime) {
        this.value = value;
        this.expiryTime = System.currentTimeMillis() + expiryTime;
    }

    public T getValue() {
        return value;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}