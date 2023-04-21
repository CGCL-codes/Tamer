    public void disconnect() {
        SharedObjectMessage msg = new SharedObjectMessage(name, 0, isPersistentObject());
        msg.addEvent(new SharedObjectEvent(Type.SERVER_DISCONNECT, null, null));
        Channel c = ((RTMPConnection) source).getChannel((byte) 3);
        c.write(msg);
        notifyDisconnect();
        initialSyncReceived = false;
    }
