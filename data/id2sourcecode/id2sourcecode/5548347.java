    protected boolean loadData(URL url) {
        if (url == null) return false;
        log.finer("Resource Bundle Reading data from " + ((url == null) ? "null url" : url.toString()));
        try {
            byte[] data = CBUtility.readStream(url.openStream());
            String text = CBUtility.readI18NByteArray(data);
            return parseData(text);
        } catch (Exception e) {
            log.log(Level.FINER, "Unable to read data from url: " + ((url == null) ? "(null url)" : url.toString()), e);
            return false;
        }
    }
