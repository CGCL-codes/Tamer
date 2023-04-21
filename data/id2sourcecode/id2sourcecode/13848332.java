    public void copy(String orgChanPath, String aimChanPath) throws Exception {
        ChannelDAO channelDao = new ChannelDAO();
        Channel orgChannel = channelDao.getInstanceByPath(orgChanPath);
        Channel aimChannel = copySingle(orgChannel, aimChanPath);
        List list = channelDao.getChannelsList(orgChanPath);
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                copy(((Channel) list.get(i)).getPath(), aimChannel.getPath());
            }
        }
    }
