    protected boolean init(final Socket call, final DataOutputStream os, DataInputStream is) throws IOException {
        final Throwable[] syncho = new Throwable[] { null };
        writeConnection = new Thread() {

            public void run() {
                writeConnectionMessage(syncho, call, os);
            }
        };
        writeConnection.start();
        readConnectionMessage(syncho, call, is);
        boolean linked = connectContent();
        connecting = false;
        if (linked) {
            running = true;
            if (confirmRegister()) {
                runSendingLoop();
                return true;
            }
        } else {
            unregister(null);
        }
        if (!keepSocket()) {
            call.close();
        }
        return false;
    }
