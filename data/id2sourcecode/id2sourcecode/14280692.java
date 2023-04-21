    public void databasePut(String family, String key, String value) throws AgiException {
        getChannel().databasePut(family, key, value);
    }
