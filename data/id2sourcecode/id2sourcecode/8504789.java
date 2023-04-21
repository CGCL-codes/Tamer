    public void startService() {
        try {
            initChannel();
            new Thread(new Sender()).start();
            if (!writeOnly) new Thread(new Receiver()).start();
        } catch (Exception e) {
            getLog().warn("error starting service", e);
        }
    }
