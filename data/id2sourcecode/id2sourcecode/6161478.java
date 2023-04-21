    protected void registerClient(KernelAdapter ka, Endpoint p, ClientRegistrationMessage m) {
        Connection addedConnection = null;
        synchronized (this) {
            long tempId = m.getId();
            Connection c = connecting.remove(tempId);
            if (c == null) {
                c = new Connection(channels.size());
                log.log(Level.FINE, "Registering client for endpoint, pass 1:{0}.", p);
            } else {
                log.log(Level.FINE, "Refining client registration for endpoint:{0}.", p);
            }
            int channel = getChannel(ka);
            c.setChannel(channel, p);
            log.log(Level.FINE, "Setting up channel:{0}", channel);
            if (channel == CH_RELIABLE) {
                if (!getGameName().equals(m.getGameName()) || getVersion() != m.getVersion()) {
                    log.log(Level.INFO, "Kicking client due to name/version mismatch:{0}.", c);
                    c.close("Server client mismatch, server:" + getGameName() + " v" + getVersion() + "  client:" + m.getGameName() + " v" + m.getVersion());
                    return;
                }
                if (!alternatePorts.isEmpty()) {
                    ChannelInfoMessage cim = new ChannelInfoMessage(m.getId(), alternatePorts);
                    c.send(cim);
                }
            }
            if (c.isComplete()) {
                if (connections.put(c.getId(), c) == null) {
                    for (Endpoint cp : c.channels) {
                        if (cp == null) continue;
                        endpointConnections.put(cp, c);
                    }
                    addedConnection = c;
                }
            } else {
                connecting.put(tempId, c);
            }
        }
        if (addedConnection != null) {
            log.log(Level.INFO, "Client registered:{0}.", addedConnection);
            m = new ClientRegistrationMessage();
            m.setId(addedConnection.getId());
            m.setReliable(true);
            addedConnection.send(m);
            fireConnectionAdded(addedConnection);
        }
    }
