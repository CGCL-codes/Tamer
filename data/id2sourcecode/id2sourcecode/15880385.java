    public InputStream getTableLists() throws IOException {
        URL url = DBCreateConfig.class.getResource("dbcreatetable.xml");
        return url.openStream();
    }
