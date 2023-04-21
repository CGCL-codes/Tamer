    public InputStream getDaoConfig(String connectionType) throws IOException {
        URL url = null;
        if (connectionType.equals(SQL.ORACLE)) {
            url = DTSWFDBCreateConfig.class.getResource("oracle.xml");
        } else if (connectionType.equals(SQL.SQL2K)) {
            url = DTSWFDBCreateConfig.class.getResource("sql2k.xml");
        } else if (connectionType.equals(SQL.CACHE)) {
            url = DTSWFDBCreateConfig.class.getResource("cache.xml");
        }
        return url.openStream();
    }
