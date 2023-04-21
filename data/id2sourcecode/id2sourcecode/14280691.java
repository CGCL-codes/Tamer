    public String databaseGet(String family, String key) throws AgiException {
        return getChannel().databaseGet(family, key);
    }
