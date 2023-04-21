    public ArrayList<Channel> getChannelList() {
        ArrayList<Channel> channels = (ArrayList<Channel>) getHibernateTemplate().find("from Channel m order by m.name");
        return channels;
    }
