    private synchronized void handleConnection(StreamConnection conn) throws IOException {
        out = conn.openOutputStream();
        log("connection open ready to write....");
        display.setCurrent(var);
        var.initSensor();
        try {
            while (true) wait();
        } catch (Exception e) {
        }
    }
