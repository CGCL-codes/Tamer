    public Boolean getStaticContent() {
        Channel channel = getChannel();
        if (channel != null) {
            return channel.getStaticContent();
        } else {
            return null;
        }
    }
