    public InputStream getDaoConfig(String connectionType) throws IOException {
        URL url = null;
        if (connectionType.equals(SQL.ORACLE)) {
            url = DTSClassifyConfig.class.getResource("classify-oracle.xml");
        } else if (connectionType.equals(SQL.SQL2K)) {
            url = DTSClassifyConfig.class.getResource("classify-sql2k.xml");
        } else if (connectionType.equals(SQL.CACHE)) {
            url = DTSClassifyConfig.class.getResource("classify-cache.xml");
        }
        return url.openStream();
    }
