    public PropertyTable createAndLoadPropertyTable(String dbName, String tableName, URL url) {
        if (url == null) return null;
        try {
            return createAndLoadPropertyTable(dbName, tableName, url.openStream());
        } catch (IOException e) {
            logger.log(Level.WARNING, "Problem opening the URL: " + url.toExternalForm(), e);
            throw new IllegalStateException("Problem opening the packed file: " + url.toExternalForm(), e);
        }
    }
