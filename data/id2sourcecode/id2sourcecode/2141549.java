    public InputStream getDaoConfig(String connectionType) throws IOException {
        URL url = null;
        if (connectionType.equals(SQL.ORACLE)) {
            url = getDaoDTDClass().getResource("oracle.xml");
        } else if (connectionType.equals(SQL.SQL2K)) {
            url = getDaoDTDClass().getResource("sql2k.xml");
        }
        return url.openStream();
    }
