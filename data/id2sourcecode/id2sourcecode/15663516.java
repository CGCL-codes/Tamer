        public void put(D x) throws InterruptedException {
            lock.lock();
            try {
                putLocal(x);
                for (HGPeerIdentity target : getChannelPeers(getId())) {
                    Message msg = createMessage(Performative.InformRef, activity);
                    try {
                        if (!thisPeer.getPeerInterface().send(thisPeer.getNetworkTarget(target), combine(msg, struct(CONTENT, struct("datum", activity.toJson(x), "channel", getId())))).get()) throw new PeerFailedException(target, " while sending data on channel " + getId());
                    } catch (ExecutionException ex) {
                        throw new PeerFailedException(target, " while sending data on channel " + getId(), ex);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException("Don't know what to do with thread interrupts yet.");
                    }
                }
            } finally {
                lock.unlock();
            }
        }
