    public Channel getChannel(String name) {
        ArrayList<Channel> list = (ArrayList<Channel>) getHibernateTemplate().find("from Channel m where m.name=?", name);
        Channel channel = null;
        if (list.isEmpty()) {
            channel = null;
        } else {
            channel = (Channel) list.get(0);
        }
        return channel;
    }
