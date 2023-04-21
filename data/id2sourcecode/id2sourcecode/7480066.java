    public InputStream findResourceStream(String name) {
        URL url = findResource(name);
        if (url == null) {
            return null;
        }
        try {
            return url.openStream();
        } catch (IOException ioex) {
            logObj.debug("Error reading URL, ignoring", ioex);
            return null;
        }
    }
