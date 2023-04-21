    public static void readWriteCharUtil(String sxURL, String resource, Writer writer) throws IOException {
        sxURL = getResourceURL(sxURL, resource);
        readWriteCharUtil(new URL(sxURL), writer);
    }
