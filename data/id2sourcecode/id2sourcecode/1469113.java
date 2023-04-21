    public InputStream getTableLists() throws IOException {
        URL url = DBCreateConfig.class.getResource("dbloadtable.xml");
        return url.openStream();
    }
