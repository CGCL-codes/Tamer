    public static void startGCSpyServer() throws VM_PragmaInterruptible {
        int port = getGCSpyPort();
        Log.write("GCSpy.startGCSpyServer, port=", port);
        Log.write(", wait=");
        Log.writeln(getGCSpyWait());
        if (port > 0) {
            Plan.startGCSpyServer(port, getGCSpyWait());
            Log.writeln("gcspy thread booted");
        }
    }
