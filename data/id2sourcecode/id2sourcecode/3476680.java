    public InputStream getDaoConfig(String connectionType) throws IOException {
        URL url = null;
        if (connectionType.equals(SQL.ORACLE)) {
            url = DTSOntyExtConConfig.class.getResource("ontyextconoracle.xml");
        } else if (connectionType.equals(SQL.SQL2K)) {
            url = DTSOntyExtConConfig.class.getResource("ontyextconsql2k.xml");
        } else if (connectionType.equals(SQL.CACHE)) {
            url = DTSOntyExtConConfig.class.getResource("ontyextconcache.xml");
        }
        return url.openStream();
    }
