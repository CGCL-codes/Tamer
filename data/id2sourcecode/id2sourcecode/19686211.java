    @SuppressWarnings("unchecked")
    public ConfigurationSapImpl(String channelId) throws ResourceException {
        final Tracer tracer = baseTracer.entering("ConfigurationSapImpl(String channelId)");
        this.receiverChannels = new LinkedList();
        this.senderChannels = new LinkedList();
        this.lookupManager = LookupManager.getInstance();
        tracer.info("LookupManager.getInstance(): {0}", this.lookupManager);
        try {
            tracer.info("this.lookupManager.getChannelsByAdapterType(" + AS2Util.ADAPTER_NAME + ", " + AS2Util.ADAPTER_NAMESPACE + ")");
            LinkedList allChannels = this.lookupManager.getChannelsByAdapterType(AS2Util.ADAPTER_NAME, AS2Util.ADAPTER_NAMESPACE);
            tracer.info("The XI CPA service returned {0} channels for adapter type {1} with namespace {2}", new Object[] { new Integer(allChannels.size()), AS2Util.ADAPTER_NAME, AS2Util.ADAPTER_NAMESPACE });
            for (int i = 0; i < allChannels.size(); i++) {
                Channel _channel = (Channel) allChannels.get(i);
                tracer.info("Found channel {0} with direction {1}", new Object[] { _channel.getChannelName(), _channel.getDirection() });
                if (_channel.getDirection() == Direction.OUTBOUND) {
                    this.receiverChannels.add(_channel);
                } else if (_channel.getDirection() == Direction.INBOUND) {
                    this.senderChannels.add(_channel);
                }
                tracer.info("Channel with ID {0} for party {1} and service {2} added (direction is {3}).", new Object[] { _channel.getObjectId(), _channel.getParty(), _channel.getService(), _channel.getDirection().toString() });
            }
        } catch (Exception e) {
            tracer.catched(e);
            ResourceException re = new ResourceException("CPA lookup failed: " + e.getMessage());
            tracer.throwing(re);
            throw re;
        }
        this.channelId = channelId;
        tracer.leaving();
    }
