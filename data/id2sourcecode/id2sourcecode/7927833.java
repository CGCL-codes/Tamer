    @SuppressWarnings("unchecked")
    public <D> Channel<D> getChannel(String id) {
        return (Channel<D>) channels.get(id);
    }
