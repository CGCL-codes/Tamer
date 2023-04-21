    public EnhancementTable(URL url) throws McIDASException {
        try {
            dataStream = new DataInputStream(new BufferedInputStream(url.openStream()));
        } catch (Exception e) {
            throw new McIDASException("Unable to open enhancement table at URL" + url);
        }
        readRGBValues();
    }
