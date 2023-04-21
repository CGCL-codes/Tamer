    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            super.afterPropertiesSet();
            ApplicationContext applicationContext = this.getApplicationContext();
            Collection<Channel> channels = this.getChannels();
            long now = System.currentTimeMillis();
            Map<String, CircleOfTrustManager> cots = applicationContext.getBeansOfType(CircleOfTrustManager.class);
            if (cots.values().size() > 1) throw new IdentityMediationException("Multiple Circle of Trust managers not supported!");
            CircleOfTrustManager cotMgr = null;
            if (cots.values().size() < 1) {
                logger.debug("No Circle of Trust manager found");
            } else {
                cotMgr = cots.values().iterator().next();
                if (logger.isDebugEnabled()) logger.debug("Initializing Mediation infrastructure using COT manager " + cotMgr);
                cotMgr.init();
            }
            Set<String> channelNames = new HashSet<String>();
            Set<String> endpointNames = new HashSet<String>();
            for (Channel channel : channels) {
                if (channel.getUnitContainer() == null) {
                    throw new IllegalArgumentException("Channel " + channel.getName() + " [" + channel.getClass().getSimpleName() + "] does not have a mediation unitContainer!");
                }
                if (channel.getName() == null) {
                    throw new IllegalArgumentException("Channel " + channel + " name cannot be null");
                }
                if (channelNames.contains(channel.getName())) {
                    throw new IllegalArgumentException("Channel name already in use " + channel.getName());
                }
                channelNames.add(channel.getName());
                IdentityMediationUnitContainer unitContainer = channel.getUnitContainer();
                IdentityMediator mediator = channel.getIdentityMediator();
                logger.info("Registering channel " + channel + " with mediator/unitContainer " + mediator + "/" + unitContainer);
                channel.getUnitContainer().getMediators().add(mediator);
                if (channel instanceof FederationChannel) {
                    logger.info("Registering Federation channel " + channel);
                    if (cotMgr == null) {
                        logger.error("No circle of trust defined. Federation features cannot be configured for channel " + channel.getName() + " !!!");
                        continue;
                    }
                    AbstractFederationChannel fedChannel = (AbstractFederationChannel) channel;
                    MetadataEntry md = cotMgr.findEntityRoleMetadata(fedChannel.getMember().getAlias(), fedChannel.getRole());
                    if (md != null) {
                        fedChannel.setMetadata(md);
                    }
                    fedChannel.setCircleOfTrust(cotMgr.getCot());
                    if (fedChannel.getEndpoints() != null) {
                        for (IdentityMediationEndpoint identityMediationEndpoint : fedChannel.getEndpoints()) {
                            IdentityMediationEndpointImpl endpoint = (IdentityMediationEndpointImpl) identityMediationEndpoint;
                            if (endpoint.getName() == null) throw new IllegalArgumentException("Endpoint name cannot be null " + endpoint);
                            if (endpointNames.contains(endpoint.getName())) {
                                throw new IllegalArgumentException("Endpoint name already in use " + endpoint.getName());
                            }
                            endpointNames.add(endpoint.getName());
                            MetadataEntry endpointMetadata = cotMgr.findEndpointMetadata(fedChannel.getMember().getAlias(), fedChannel.getRole(), new EndpointDescriptorImpl(identityMediationEndpoint.getName(), identityMediationEndpoint.getType(), identityMediationEndpoint.getBinding()));
                            endpoint.setMetadata(endpointMetadata);
                        }
                    } else {
                        logger.warn("Federation channel does not define endpoints : " + fedChannel.getName());
                    }
                }
            }
            IdentityMediationUnitContainer container = this.getContainer();
            container.init(this);
            for (Channel channel : channels) {
                if (channel.getIdentityMediator() != null) {
                    logger.info("Setting up endpoints for channel : " + channel.getName());
                    IdentityMediator mediator = channel.getIdentityMediator();
                    mediator.setupEndpoints(channel);
                } else {
                    logger.warn("Channel does not have an Identity Mediator");
                }
            }
            container.start();
            logger.info("IDBus Identity Mediation Unit '" + getName() + "' started in " + (System.currentTimeMillis() - now) + "ms");
            System.out.println("IDBus Identity Mediation Unit '" + getName() + "' started in " + (System.currentTimeMillis() - now) + "ms");
        } catch (Exception e) {
            System.err.println("IDBus Identity Mediation Unit '" + getName() + "' initialization: " + e.getMessage());
            throw new IdentityMediationException("IDBus Identity Mediation Unit '" + getName() + "' initialization error:" + e.getMessage(), e);
        }
    }
