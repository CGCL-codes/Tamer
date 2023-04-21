    public static synchronized void dump(PrintWriter writer) {
        DaemonRecord record;
        writer.println("Daemon master managing " + _instance._count + " daemons");
        record = _instance._first;
        while (record != null) {
            writer.println("  " + record._name);
            writer.println("    Thread:   " + record._thread);
            writer.println("    Runnable: " + record._runnable);
            writer.println("    Priority: " + record._priority);
            record = record._next;
        }
    }
