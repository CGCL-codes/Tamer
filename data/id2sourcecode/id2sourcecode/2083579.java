    protected void prepareModel() throws Exception {
        if (id != null) {
            entity = channelService.getChannel(id);
        } else {
            entity = new Channel();
        }
    }
