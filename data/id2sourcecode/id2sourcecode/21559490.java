    @Override
    public void incomingConnectionReceived(PeerConnection peer) {
        synchronized (remainingPeers) {
            if (!connectedPeers.contains(peer)) {
                if (remainingPeers.contains(peer)) {
                    remainingPeers.remove(peer);
                }
                connectedPeers.add(peer);
                peer.enqueue(new PendingHandshake(metadata.getInfoHash(), ConfigManager.getClientId().getBytes()));
                logger.fine("New peer (incoming connection) for download " + metadata.getName() + ", " + peer.getPeer());
                try {
                    peer.getChannel().configureBlocking(false);
                    peer.getChannel().register(sockSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, peer);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
