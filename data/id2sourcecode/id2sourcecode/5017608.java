    public synchronized Object get_event() {
        if (size == 0) return null;
        Object event = queue[0];
        size--;
        for (int i = 0; i < size; i++) queue[i] = queue[i + 1];
        queue[size] = null;
        return event;
    }
