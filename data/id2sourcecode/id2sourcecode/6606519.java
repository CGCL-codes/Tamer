    public InputStream getTableLists() throws IOException {
        URL url = DTSWFDBCreateConfig.class.getResource("dtswfdbcreatetable.xml");
        return url.openStream();
    }
