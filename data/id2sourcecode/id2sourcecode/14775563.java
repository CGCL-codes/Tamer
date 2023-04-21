    public ArrayList<Channel> getChannelList(Member member) {
        ArrayList<Channel> channels = (ArrayList<Channel>) getHibernateTemplate().find("from Channel c,Member m where m.channels=c.id and m.id=? order by c.name", member.getId());
        return channels;
    }
