    public void run() {
        synchronized (ioSessions) {
            for (Iterator itor = ioSessions.iterator(); itor.hasNext(); ) {
                try {
                    StdChannelImpl ioSession = (StdChannelImpl) itor.next();
                    ioSession.send(socket);
                } catch (SocketTimeoutException ex) {
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        try {
            socket.receive(packet);
            StdChannelImpl ioSession = (StdChannelImpl) getChannel(packet.getSocketAddress());
            if (ioSession == null) {
                ioSession = new StdChannelImpl(this, packet.getSocketAddress());
                ioSessions.add(ioSession);
                pendingSessions.add(ioSession);
            }
            ioSession.receive(socket, packet);
        } catch (SocketTimeoutException ex) {
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
