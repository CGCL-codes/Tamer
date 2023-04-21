    public InputStream getDaoConfig(String connectionType) throws IOException {
        URL url = null;
        if (connectionType.equals(SQL.ORACLE)) {
            url = DBSubscriptionConfig.class.getResource("subscription-oracle.xml");
        } else if (connectionType.equals(SQL.SQL2K)) {
            url = DBSubscriptionConfig.class.getResource("subscription-sql2k.xml");
        }
        return url.openStream();
    }
