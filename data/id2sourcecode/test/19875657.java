    private void setChannelName(final PatchDetail detail, final String name) {
        Channel channel = detail.getDimmer().getChannel();
        channel.removeNameListener(this);
        channel.setName(name);
        channel.addNameListener(this);
    }
