    public int getChannelCount() {
        List list = getHibernateTemplate().find("select count(*) from RSSChannel channel");
        if (list.size() > 0) return (Integer) list.get(0);
        return 0;
    }
