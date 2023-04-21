    public static Number[] add(Key key, long c, double d) {
        if (!writeThread.isAlive()) runWriteThread();
        Value value = getValue(key);
        return value.add(c, d);
    }
