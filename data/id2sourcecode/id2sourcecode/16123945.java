    public BusGroupImpl(BundleContext context, final Dictionary properties) throws ChannelException {
        topic = (String) properties.get(Group.TOPIC);
        registration = context.registerService(Group.class.getName(), this, properties);
        ServiceReference reference = context.getServiceReference(PeerAdmin.class.getName());
        peerAdmin = (PeerAdmin) context.getService(reference);
        reference = context.getServiceReference(LogService.class.getName());
        log = (LogService) context.getService(reference);
        String nameString = context.getProperty(PeerAdmin.KEY_LOCALNAME);
        if (nameString != null) localProperties.put(Peer.NAME, nameString);
        String hostString = context.getProperty(PeerAdmin.KEY_LOCALHOSTNAME);
        if (hostString != null) localProperties.put(Peer.HOST, hostString);
        localProperties.put(Peer.PROTOCOL, JGroupsConstants.PROTOCOL_JGROUPS);
        this.consumer = new Consumer() {

            public Serializable getCache() {
                return (Serializable) localProperties;
            }

            public void handleNotification(Serializable n) {
                log.log(LogService.LOG_DEBUG, "notification");
                Dictionary d = (Dictionary) n;
                String type = (String) d.get(PeerEvent.EVENT_TYPE);
                if (PeerEvent.EVENT_STATUS.equals(type)) {
                    handleStatusEvent(d);
                } else {
                    sendEvent(type, d);
                }
            }

            public void memberJoined(Address mbr) {
                if (bus.getLocalAddress().equals(mbr)) {
                    addPeer(localPeer);
                    return;
                }
                log.log(LogService.LOG_DEBUG, "memberJoined");
                Peer peer = new BusPeerImpl(mbr, BusGroupImpl.this);
                addPeer(peer);
                sendStatusEvent(PeerEvent.STATUS_GROUPUPDATED);
            }

            public void memberLeft(Address mbr) {
                if (bus.getLocalAddress().equals(mbr)) {
                    removePeer(localPeer);
                    return;
                }
                log.log(LogService.LOG_DEBUG, "memberLeft");
                Peer peer = new BusPeerImpl(mbr, BusGroupImpl.this);
                removePeer(peer);
                sendStatusEvent(PeerEvent.STATUS_GROUPUPDATED);
            }
        };
        this.listener = new ChannelListener() {

            public void channelClosed(Channel channel) {
                log.log(LogService.LOG_INFO, "group closed " + topic);
            }

            public void channelConnected(Channel channel) {
                if (isOwner()) {
                    log.log(LogService.LOG_INFO, "created group " + topic);
                } else {
                    log.log(LogService.LOG_INFO, "joined group " + topic);
                }
                Vector membership = bus.getMembership();
                for (Iterator iter = membership.iterator(); iter.hasNext(); ) {
                    Address address = (Address) iter.next();
                    if (!bus.getLocalAddress().equals(address)) addPeer(new BusPeerImpl(address, BusGroupImpl.this));
                }
            }

            public void channelDisconnected(Channel channel) {
                log.log(LogService.LOG_INFO, "disconnected from " + topic);
            }

            public void channelReconnected(Address addr) {
                log.log(LogService.LOG_INFO, "reconnected to " + topic);
            }

            public void channelShunned() {
                log.log(LogService.LOG_INFO, "shunned " + topic);
                bus.getChannel().disconnect();
                try {
                    bus.getChannel().connect(topic);
                } catch (ChannelClosedException e) {
                    log.log(LogService.LOG_ERROR, "Error reconnecting to " + topic, e);
                } catch (ChannelException e) {
                    log.log(LogService.LOG_ERROR, "Error reconnecting to " + topic, e);
                }
            }
        };
    }
