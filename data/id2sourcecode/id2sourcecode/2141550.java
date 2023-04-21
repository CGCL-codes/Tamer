    public InputStream getTableLists() throws IOException {
        URL url = getDaoDTDClass().getResource("dbmigratetable.xml");
        return url.openStream();
    }
