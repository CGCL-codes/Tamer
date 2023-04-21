    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<ChannelImpl> getChannel() {
        List<ChannelImpl> cil = new ArrayList<ChannelImpl>();
        for (Channel ch : Backyard.channelhandler.getAllChannel()) {
            cil.add((ChannelImpl) ch);
        }
        return cil;
    }
