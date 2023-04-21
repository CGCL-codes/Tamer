        public void put(D x) throws InterruptedException {
            lock.lock();
            try {
                putLocal(x);
                for (HGPeerIdentity target : getChannelPeers(getId())) {
                    HGHandle jobId = getJob().getHandle();
                    if (jobId == null) throw new RuntimeException("Attempt to put data into a channel with no associated peer task id.");
                    Message msg = createMessage(Performative.InformRef, activity);
                    try {
                        if (!thisPeer.getPeerInterface().send(thisPeer.getNetworkTarget(target), combine(msg, struct(CONTENT, struct("datum", activity.toJson(x), "channel", list(jobId, getId()))))).get()) throw new PeerFailedException(target, " while sending data on channel " + getId());
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
