    public void handle(Transaction request) throws Exception {
        Entity entity = request.getEntity();
        Storage storage = request.getStorage();
        Channel channel = request.getChannel();
        handle(entity, storage, channel);
    }
