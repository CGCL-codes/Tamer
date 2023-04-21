    protected void constructFeeder(FeederDaemonConfig config) {
        LOG.info("Debug: filepath: " + filepath);
        channel = new RSSChannel();
        channel.setDescription(config.getChannelDescription());
        LOG.info("Debug: description: " + config.getChannelDescription());
        channel.setLink(config.getChannelLink());
        channel.setTitle(config.getChannelTitle());
        channel.setCopyright(config.getChannelCopyright());
        channel.setManagingEditor(config.getChannelEditor());
        channel.setWebMaster(config.getChannelWebmaster());
        RSSImage image = new RSSImage();
        image.setTitle(config.getChannelTitle());
        image.setLink(config.getChannelLink());
        image.setUrl(config.getChannelImage());
        channel.setRSSImage(image);
        LOG.info("Debug: Constructed");
    }
