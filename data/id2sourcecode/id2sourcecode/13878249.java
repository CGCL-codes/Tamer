    public List getChannelListByMember(Member member) {
        List list = null;
        try {
            list = channelManager.getChannelList(member);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
        return list;
    }
