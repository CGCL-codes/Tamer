    public void start() {
        try {
            ZipInputStream zis = new ZipInputStream(url.openStream());
            zis.getNextEntry();
            ois = new ObjectInputStream(zis);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }
        bSecure = url.getProtocol().equalsIgnoreCase("https");
        if (null != cel) {
            cel.connected(bSecure);
        }
        dateStart = new Date();
        bContinue = true;
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }
