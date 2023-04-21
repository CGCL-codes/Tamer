    public QueryInfo getInfo() throws IOException {
        QueryInfo info = new QueryInfo();
        info.setHostname(hostname);
        info.setVersion(getVersion());
        info.setPing(getPing());
        info.setChannels(getChannels());
        info.setPlayers(getPlayers());
        return info;
    }
