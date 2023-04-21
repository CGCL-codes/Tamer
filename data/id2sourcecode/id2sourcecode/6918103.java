    @Override
    public void addChannel(BloglinesSite channel) {
        if (getChannelList() == null) {
            ArrayList<BloglinesSite> list = new ArrayList<BloglinesSite>();
            setChannelList(list);
        }
        getChannelList().add(channel);
    }
