    protected void doUnbind(String name, boolean closeAll) throws IOException {
        DatagramChannel dc = null;
        Listener listener = null;
        SelectionKey selectionKey = null;
        listener = container.getListener(name);
        dc = (DatagramChannel) listener.getChannel();
        if (dc != null) {
            selectionKey = dc.keyFor(selector);
            if (selectionKey.attachment() instanceof Listener) {
                selectionKey.attach(null);
                container.sendEvent(new ListenerEvent(EventCatagory.STATUS, SystemEventType.UNBIND, "unbind", container.getName(), name));
                if (dc != null) {
                    dc.socket().close();
                }
            } else {
                container.sendEvent(new ConnectionManagerEvent(EventCatagory.ERROR, SystemEventType.UNBIND, "Selection Key is not of type Listener", container.getName()));
            }
            if (closeAll) {
                doCloseAll(name);
            }
        } else {
            container.sendEvent(new ListenerEvent(EventCatagory.ERROR, SystemEventType.EXCEPTION, "Listener Channel is null", container.getName(), name));
        }
    }
