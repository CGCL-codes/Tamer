    private DirectConnection getConnection(String neighbor) {
        DirectConnection connection = null;
        for (int index = 0; index < directConnections.size(); index++) {
            connection = (DirectConnection) directConnections.get(index);
            if (connection.getPeerId().equalsIgnoreCase(neighbor)) return connection;
            connection = null;
        }
        Peer peer = peerService.getNeighbor(neighbor);
        if (peer instanceof Peer) {
            try {
                connection = new DirectConnection(peer.getIp(), peer.getPort(), peer.getId(), sson.getNetworkId());
                synchronized (directConnections) {
                    directConnections.add(connection);
                }
                channelListener.addChannel(connection.getChannel());
                log.debug("Created a direct connection with peer " + peer.getId());
            } catch (SeppCommunicationException e) {
                log.debug(e.getMessage());
            }
        }
        if (!(connection instanceof DirectConnection)) {
            peerService.setNeighborStatus(neighbor, false);
        }
        return connection;
    }
