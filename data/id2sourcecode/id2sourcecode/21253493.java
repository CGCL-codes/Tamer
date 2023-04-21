    public ChannelIF handle(String content) throws ContentHandlerException {
        log.trace("DummyContentHandler handling request ... ");
        this.setTitle("Unnamed Feed");
        this.setDescription("This is a dummy feed for test");
        try {
            this.setLocation(new URL("http://hi"));
        } catch (MalformedURLException me) {
            throw new ContentHandlerException("Error Handling Content", me);
        }
        try {
            this.addItem("Test item", "Test item desc", new URL("http://foo"));
            this.addItem("Test item (2)", "Test item desc, again", new URL("http://foo2"));
        } catch (MalformedURLException e) {
            throw new ContentHandlerException("Error Handling Content", e);
        }
        log.trace("DummyContentHandler finished handling request.");
        return this.getChannel();
    }
