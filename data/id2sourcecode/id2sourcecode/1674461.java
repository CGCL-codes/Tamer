    protected void checkChannelOrRedirect(WebContext context) {
        Channel channel = context.getChannel();
        if (channel == null) throw new HttpSeeOtherException("Channel not open.", "/");
    }
