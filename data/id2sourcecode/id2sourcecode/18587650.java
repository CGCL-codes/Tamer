    public boolean writeRequest(String oid, TypeDescription type, String function, ThreadId tid, Object[] arguments) throws IOException {
        if (oid.equals(PROPERTIES_OID)) {
            throw new IllegalArgumentException("illegal OID " + oid);
        }
        synchronized (monitor) {
            while (!initialized) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e.toString());
                }
            }
            return writeRequest(false, oid, type, function, tid, arguments);
        }
    }
