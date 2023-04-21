    public void socketReadyForConnect() {
        try {
            if (!isOpen()) return;
            if (getChannel().finishConnect()) {
                deleteInterest(SelectionKey.OP_CONNECT);
                m_timeOpened = System.currentTimeMillis();
                notifyObserverOfConnect();
            }
        } catch (Exception e) {
            close(e);
        }
    }
