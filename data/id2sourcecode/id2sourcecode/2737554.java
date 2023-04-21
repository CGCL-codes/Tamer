    public Channel getChannel(String rssURL) throws IOException, SAXException {
        Channel channel = null;
        RSSDigester digester = new RSSDigester();
        channel = (Channel) digester.parse(rssURL);
        String url = "http://www.openamf.org/javadocs/index.html";
        String desc = channel.getDescription() + "<br/><br/>Click <A href='" + url + "' target='_blank'><U>here</U></A> to view Java Doc's";
        channel.setDescription(desc);
        return channel;
    }
