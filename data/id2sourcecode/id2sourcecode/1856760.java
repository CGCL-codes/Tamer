    public InputStream getDaoConfig(String connectionType) throws IOException {
        URL url = null;
        if (connectionType.equals(SQL.ORACLE)) {
            url = UserManagerConfig.class.getResource("usermanageroracle.xml");
        } else if (connectionType.equals(SQL.SQL2K)) {
            url = UserManagerConfig.class.getResource("usermanagersql2k.xml");
        } else if (connectionType.equals(SQL.CACHE)) {
            url = UserManagerConfig.class.getResource("usermanagercache.xml");
        }
        return url.openStream();
    }
