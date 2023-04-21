    public InputStream getTableLists() throws IOException {
        URL url = DBSubscriptionConfig.class.getResource("subscription-table.xml");
        return url.openStream();
    }
