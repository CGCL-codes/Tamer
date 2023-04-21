    public InputStream getDaoConfig(String connectionType) throws IOException {
        URL url = null;
        if (connectionType.equals(SQL.ORACLE)) {
            url = DTSDBConfig.class.getResource("oracle.xml");
        } else if (connectionType.equals(SQL.SQL2K)) {
            url = DTSDBConfig.class.getResource("sql2k.xml");
        } else if (connectionType.equals(SQL.CACHE)) {
            url = DTSDBConfig.class.getResource("cache.xml");
        }
        return url.openStream();
    }
