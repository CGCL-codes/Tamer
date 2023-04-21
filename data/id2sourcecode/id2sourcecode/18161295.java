    public void sendFile(File file, ContentName ccnName) {
        htmlPane.setText("Writing " + file.getName() + " to CCN as: " + ccnName);
        ContentWriter writer = new ContentWriter(_handle, ccnName, file, htmlPane);
        Thread t = new Thread(writer);
        t.start();
    }
