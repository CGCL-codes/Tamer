    public void testInsertFindAndDelete() {
        ChannelController channelController = (ChannelController) context.getBean("channelController");
        Channel channel = new Channel();
        channel.setUrl("http://localhost/springworkflow/index.html");
        channel.setHtml(true);
        channelController.update(channel);
        Channel insertedChannel = channelController.getChannel(channel.getId());
        assertTrue(insertedChannel.getUrl().equals(channel.getUrl()));
        List results = channelController.getChannelsByUrl("http://localhost/springworkflow/index.html");
        assertTrue(results.size() == 1);
        Channel foundChannel = (Channel) results.get(0);
        assertTrue(foundChannel.getUrl().equals(insertedChannel.getUrl()));
        assertTrue(channelController.getAllItems(foundChannel.getId()).size() == 0);
        channelController.remove(foundChannel);
        results = channelController.getChannelsByUrl("http://localhost/springworkflow/index.html");
        assertTrue(results.size() == 0);
    }
