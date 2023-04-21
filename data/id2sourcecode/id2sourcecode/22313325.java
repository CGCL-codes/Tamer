    public void run() {
        try {
            setMainState(EMainState.connecting);
            connection = new XMPPConnection(accountPrefs.getServer());
            connection.connect();
            connection.login(accountPrefs.getLogin(), accountPrefs.getPasswd());
            setMainState(EMainState.online);
            while (Thread.currentThread() == connectionThread) {
                try {
                    synchronized (connectionThread) {
                        connectionThread.wait();
                    }
                    if (threadOperation != null) threadOperation.run();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            connection.disconnect();
            connection = null;
            setMainState(EMainState.offline);
        } catch (Exception ex) {
            System.err.println("Exception caughted: " + ex);
            ex.printStackTrace();
            connection = null;
            setMainState(EMainState.offline);
        }
    }
