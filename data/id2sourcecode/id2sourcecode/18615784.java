    public void dontPollAnymore(Long id) {
        Channel channel = this.getChannel(id);
        channel.setRemove(true);
        this.update(channel);
    }
