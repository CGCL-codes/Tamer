    private void register(Action action, int require) throws IOException {
        SelectableChannel channel = action.getChannel();
        SelectionKey key = table.remove(channel);
        if (key != null) {
            key.interestOps(require);
            key.attach(action);
        } else {
            if (channel.isOpen()) {
                select(channel, require).attach(action);
            }
        }
    }
