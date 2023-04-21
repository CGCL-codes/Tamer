    public InputStream getDaoConfig(String connectionType) throws IOException {
        URL url = null;
        if (connectionType.equals(SQL.ORACLE)) {
            url = DTSClassifyDetailsConfig.class.getResource("classifydetails-oracle.xml");
        } else if (connectionType.equals(SQL.SQL2K)) {
            url = DTSClassifyDetailsConfig.class.getResource("classifydetails-sql2k.xml");
        } else if (connectionType.equals(SQL.CACHE)) {
            url = DTSClassifyDetailsConfig.class.getResource("classifydetails-cache.xml");
        }
        return url.openStream();
    }
