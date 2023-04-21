    public SelectionKey register(Handler handler) {
        SelectionKey key = null;
        try {
            key = handler.getChannel().register(selector, handler.getCriteria(), handler);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
        return key;
    }
