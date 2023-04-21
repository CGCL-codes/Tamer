    private void register(Event event, int require) throws Exception {
        SelectableChannel channel = event.getChannel();
        SelectionKey key = table.remove(channel);
        if (key != null) {
            key.interestOps(require);
            key.attach(event);
        } else {
            if (channel.isOpen()) {
                select(channel, require).attach(event);
            }
        }
    }
