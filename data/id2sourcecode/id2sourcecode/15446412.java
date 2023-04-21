    public void registerAdapter(SPIManagedConnectionFactory spiManagedConnectionFactory) throws ResourceException {
        final Tracer tracer = baseTracer.entering("registerAdapter(SPIManagedConnectionFactory spiManagedConnectionFactory)");
        this.spiManagedConnectionFactory = spiManagedConnectionFactory;
        synchronized (this) {
            this.inboundChannels = new LinkedList();
            this.outboundChannels = new LinkedList();
            try {
                LinkedList allChannels = this.cpaLookupManager.getChannelsByAdapterType(this.adapterType, this.adapterNameSpace);
                tracer.debug("XI CPA service - number of channels: {0} for adapter type {1} with namespace {2}", new Object[] { new Integer(allChannels.size()), this.adapterType, this.adapterNameSpace });
                for (int i = 0; i < allChannels.size(); i++) {
                    Channel channel = (Channel) allChannels.get(i);
                    String status = channel.getValueAsString("adapterStatus");
                    if (channel.getDirection() == Direction.INBOUND) {
                        this.inboundChannels.add(channel);
                    } else if (channel.getDirection() == Direction.OUTBOUND) {
                        this.outboundChannels.add(channel);
                    } else continue;
                    tracer.debug("Channel with ID {0} for party {1} and service {2} added (direction is {3}, status: {4}).", new Object[] { channel.getObjectId(), channel.getParty(), channel.getService(), channel.getDirection().toString(), status });
                }
            } catch (Exception e) {
                tracer.catched(e);
                ResourceException re = new ResourceException("CPA lookup failed: " + e.getMessage());
                tracer.throwing(re);
                throw re;
            }
            try {
                this.channelLocalizer = ChannelLocalization.getLocalizationCallback();
                AdapterRegistryFactory adapterRegistryFactory = AdapterRegistryFactory.getInstance();
                this.adapterRegistry = adapterRegistryFactory.getAdapterRegistry();
                this.adapterRegistry.registerAdapter(this.adapterNameSpace, this.adapterType, new AdapterCapability[] { AdapterCapability.PUSH_PROCESS_STATUS }, new AdapterCallback[] { this });
                tracer.debug("Adapter registered for pushing process state. Adapter namespace: {0}, adapter type: {1}.", new Object[] { this.adapterNameSpace, this.adapterType });
            } catch (AdapterAlreadyRegisteredException aare) {
                tracer.catched(aare);
                stop();
                this.adapterRegistry.registerAdapter(this.adapterNameSpace, this.adapterType, new AdapterCapability[] { AdapterCapability.PUSH_PROCESS_STATUS }, new AdapterCallback[] { this });
                tracer.debug("Adapter registered for pushing process state. Adapter namespace: {0}, adapter type: {1}.", new Object[] { this.adapterNameSpace, this.adapterType });
            } catch (Exception ex) {
                tracer.catched(ex);
                ResourceException re = new ResourceException("XI AAM registration error: " + ex.getMessage());
                tracer.throwing(re);
                throw re;
            }
        }
        tracer.leaving();
    }
