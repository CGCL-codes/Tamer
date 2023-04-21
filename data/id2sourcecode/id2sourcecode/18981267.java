    public URLConnection openConnection(URL url) {
        if (TDebug.TraceCdda) {
            TDebug.out("CddaStreamHandler.openConnection():begin");
        }
        URLConnection connection = null;
        if (url.getFile().equals("")) {
            connection = new CddaDriveListConnection(url);
        } else if (url.getRef() == null) {
            connection = new CddaTocConnection(url);
        } else {
            connection = new CddaDataConnection(url);
        }
        if (TDebug.TraceCdda) {
            TDebug.out("CddaStreamHandler.openConnection():end");
        }
        return connection;
    }
