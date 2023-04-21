    public static InputStream read(String URI) {
        boolean fetchFromServer = false;
        writeToLog("* Processor requesting read for " + URI);
        URIElement element = cache.get(URI);
        if (element == null) {
            writeToLog("Cache MISS -> " + URI);
            fetchFromServer = true;
        } else if (!element.isValid()) {
            writeToLog("Cache INVALID -> " + URI);
            deleteElement(element);
            fetchFromServer = true;
        }
        if (!fetchFromServer) {
            writeToLog("Cache HIT -> " + URI);
            if (element.isInmemory()) {
                writeToLog("... Read from memory cache");
                return element.getXMLStream();
            } else {
                writeToLog("... Read from disk cache");
                return readFromFile(CACHEDIR + element.getLocalAddress());
            }
        } else {
            writeToLog("... Read from actual location");
            return readFromServer(URI);
        }
    }
