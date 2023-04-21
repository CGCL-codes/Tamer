    protected void doUnBind(String name, boolean closeAll) throws IOException {
        ServerSocketChannel ssc = null;
        Listener listener = null;
        SelectionKey selectionKey = null;
        Object o = null;
        listener = container.getListener(name);
        ssc = (ServerSocketChannel) listener.getChannel();
        selectionKey = ssc.keyFor(selector);
        if (selectionKey.attachment() instanceof Listener) {
            selectionKey.attach(null);
            if (ssc != null) {
                container.sendEvent(new ListenerEvent(EventCatagory.STATUS, SystemEventType.UNBIND, ssc.socket().getLocalSocketAddress().toString(), container.getName(), name));
                if (!ssc.isOpen()) {
                    ssc.close();
                }
            }
        } else {
            container.sendEvent(new ConnectionManagerEvent(EventCatagory.ERROR, SystemEventType.UNBIND, "Selection Key is not of type Listener", container.getName()));
        }
        if (closeAll) {
            doCloseAll(name);
        }
    }
