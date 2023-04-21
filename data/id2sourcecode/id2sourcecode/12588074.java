    public void fireEvent(final Message message) {
        Set<WeakReference<MessageListener>> set = map.get(message.getChannel());
        if (set != null) {
            pool.execute(new MessageHandler(message, set));
        }
    }
