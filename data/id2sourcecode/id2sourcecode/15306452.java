    public static Document parseXMLFromURL(URL url) throws XMLHelperException {
        try {
            URLConnection inConnection = url.openConnection();
            InputSource is = new InputSource(inConnection.getInputStream());
            return parseXMLFromInputSource(is);
        } catch (IOException ioe) {
            throw new XMLHelperException("Unable to read from source string", ioe);
        }
    }
