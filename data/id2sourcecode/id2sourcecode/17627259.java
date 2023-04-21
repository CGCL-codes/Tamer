    private void createDestination(DestinationSettings destSettings, Service service, ServiceSettings svcSettings) {
        String destId = destSettings.getId();
        Destination destination = service.createDestination(destId);
        List chanSettings = destSettings.getChannelSettings();
        if (chanSettings.size() > 0) {
            List<String> channelIds = new ArrayList<String>(2);
            for (Iterator iter = chanSettings.iterator(); iter.hasNext(); ) {
                ChannelSettings cs = (ChannelSettings) iter.next();
                channelIds.add(cs.getId());
            }
            destination.setChannels(channelIds);
        }
        SecurityConstraint constraint = destSettings.getConstraint();
        destination.setSecurityConstraint(constraint);
        destination.initialize(destId, svcSettings.getProperties());
        destination.initialize(destId, destSettings.getAdapterSettings().getProperties());
        destination.initialize(destId, destSettings.getProperties());
        createAdapter(destination, destSettings, svcSettings);
    }
