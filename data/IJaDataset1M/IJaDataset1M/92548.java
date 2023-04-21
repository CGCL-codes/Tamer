package com.once.server;

import java.util.HashMap;

public class SessionManager {

    private static SessionManager singleton;

    private HashMap<String, HashMap<String, Object>> data;

    protected SessionManager() {
        data = new HashMap<String, HashMap<String, Object>>();
        return;
    }

    private HashMap<String, Object> getSessionStorage(String session) {
        HashMap<String, Object> storage;
        if (data.containsKey(session) == false) data.put(session, storage = new HashMap<String, Object>()); else storage = data.get(session);
        return (storage);
    }

    public synchronized Object get(String session, String identifier) {
        return (getSessionStorage(session).get(identifier));
    }

    public static synchronized SessionManager getInstance() {
        return ((singleton == null) ? singleton = new SessionManager() : singleton);
    }

    public synchronized void put(String session, String identifier, Object item) {
        getSessionStorage(session).put(identifier, item);
        return;
    }

    public synchronized void remove(String session, String identifier) {
        getSessionStorage(session).remove(identifier);
        return;
    }
}
