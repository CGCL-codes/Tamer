    public void initialize(PluginInterface _pi) {
        plugin_interface = _pi;
        singleton = this;
        init_sem.release();
        try {
            RSSFeed feed = plugin_interface.getUtilities().getRSSFeed(new URL("http://aelitis.com:7979/rss_feed.xml"));
            RSSChannel[] channels = feed.getChannels();
            for (int i = 0; i < channels.length; i++) {
                RSSChannel channel = channels[i];
                System.out.println("chan: title = " + channel.getTitle() + ", desc = " + channel.getDescription() + ", link = " + channel.getLink() + ", pub = " + channel.getPublicationDate());
                RSSItem[] items = channel.getItems();
                for (int j = 0; j < items.length; j++) {
                    RSSItem item = items[j];
                    System.out.println("    item:" + item.getTitle() + ", desc = " + item.getDescription() + ", link = " + item.getLink());
                    SimpleXMLParserDocumentNode node = item.getNode();
                    System.out.println("        [hash] " + node.getChild("torrent_sha1").getValue());
                    System.out.println("        [size] " + node.getChild("torrent_size").getValue());
                    System.out.println("        [seed] " + node.getChild("torrent_seeders").getValue());
                    System.out.println("        [leec] " + node.getChild("torrent_leechers").getValue());
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
