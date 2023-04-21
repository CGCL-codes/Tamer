    public synchronized void resize(int maxItems) {
        if (maxItems < 1) {
            maxItems = 1;
        }
        buckets = new Object[maxItems + 1];
        readIndex = writeIndex = overrunCounter = 0;
    }
