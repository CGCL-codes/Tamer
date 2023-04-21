    private void disconnect(boolean verbose) {
        if (manager != null) {
            closeRepository(false);
            writeln("Disconnecting from " + managerID);
            manager.shutDown();
            manager = null;
            managerID = null;
        } else if (verbose) {
            writeln("Already disconnected");
        }
    }
