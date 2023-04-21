    public InputStream getDaoConfig(String connectionType) throws IOException {
        URL url = null;
        if (connectionType.equals(SQL.ORACLE)) {
            url = SubsetDBConfig.class.getResource("subset-oracle.xml");
        } else if (connectionType.equals(SQL.SQL2K)) {
            url = SubsetDBConfig.class.getResource("subset-sql2k.xml");
        } else if (connectionType.equals(SQL.CACHE)) {
            url = SubsetDBConfig.class.getResource("subset-cache.xml");
        }
        return url.openStream();
    }
