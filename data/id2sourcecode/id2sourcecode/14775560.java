    public Channel getChannel(int id) {
        ArrayList<Channel> list = (ArrayList<Channel>) getHibernateTemplate().find("from Channel m where m.id=?", id);
        Channel channel = null;
        if (list.isEmpty()) {
            channel = null;
        } else {
            channel = (Channel) list.get(0);
        }
        return channel;
    }
