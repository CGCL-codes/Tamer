    public synchronized void updateMode(MediaType mediaType) throws ModeNotSupportedException {
        boolean send = false;
        boolean recv = false;
        boolean loop = false;
        for (int i = 0; i < activeConnections.size(); i++) {
            if (loop) {
                break;
            }
            connection = activeConnections.get(i);
            switch(connection.getMode(mediaType)) {
                case SEND_ONLY:
                    send = true;
                    break;
                case RECV_ONLY:
                    recv = true;
                    break;
                case SEND_RECV:
                case CONFERENCE:
                    send = true;
                    recv = true;
                    break;
                case LOOPBACK:
                    loop = true;
                    break;
            }
        }
        if (loop) {
            getChannel(mediaType).setMode(ConnectionMode.LOOPBACK);
            return;
        }
        if (send && !recv) {
            getChannel(mediaType).setMode(ConnectionMode.SEND_ONLY);
            return;
        }
        if (!send && recv) {
            getChannel(mediaType).setMode(ConnectionMode.RECV_ONLY);
            return;
        }
        if (send && recv) {
            getChannel(mediaType).setMode(ConnectionMode.SEND_RECV);
            return;
        }
        if (!send && !recv) {
            getChannel(mediaType).setMode(null);
            return;
        }
    }
