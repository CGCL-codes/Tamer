    private void sendUpdates() {
        if (!ownerMessage.getEvents().isEmpty()) {
            SharedObjectMessage syncOwner = new SharedObjectMessage(null, name, version, isPersistentObject());
            syncOwner.addEvents(ownerMessage.getEvents());
            if (source != null) {
                Channel channel = ((RTMPConnection) source).getChannel((byte) 3);
                if (channel != null) {
                    channel.write(syncOwner);
                    log.debug("Owner: " + channel);
                } else {
                    log.warn("No channel found for owner changes!?");
                }
            }
            ownerMessage.getEvents().clear();
        }
        if (!syncEvents.isEmpty()) {
            for (IEventListener listener : listeners) {
                if (listener == source) {
                    log.debug("Skipped " + source);
                    continue;
                }
                if (!(listener instanceof RTMPConnection)) {
                    log.warn("Can't send sync message to unknown connection " + listener);
                    continue;
                }
                SharedObjectMessage syncMessage = new SharedObjectMessage(null, name, version, isPersistentObject());
                syncMessage.addEvents(syncEvents);
                Channel c = ((RTMPConnection) listener).getChannel((byte) 3);
                log.debug("Send to " + c);
                c.write(syncMessage);
            }
            syncEvents.clear();
        }
    }
