    void init(Event event) {
        writeValue(P_PROJ, event.readValue(Event.P_PROJ));
        writeValue(P_TASK, event.readValue(Event.P_TASK));
    }
