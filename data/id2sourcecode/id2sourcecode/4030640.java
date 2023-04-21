    public Collection getChannelList() {
        String hsql = "select ent from ChannelEntity as ent ";
        return generalDao.find(hsql);
    }
