    public ConfigMap describeService(Endpoint endpoint, boolean onlyReliable) {
        ConfigMap serviceConfig = null;
        for (Destination destination : destinations.values()) {
            boolean endpointIdMatches = false;
            if (endpoint == null) {
                endpointIdMatches = true;
            } else {
                List<String> channels = destination.getChannels();
                if (channels != null) {
                    for (String channelId : channels) {
                        if (channelId.equals(endpoint.getId())) {
                            endpointIdMatches = true;
                            break;
                        }
                    }
                }
            }
            if (endpointIdMatches) {
                ConfigMap destinationConfig = destination.describeDestination(onlyReliable);
                if (destinationConfig != null && destinationConfig.size() > 0) {
                    if (serviceConfig == null) {
                        serviceConfig = new ConfigMap();
                        serviceConfig.addProperty(ConfigurationConstants.ID_ATTR, getId());
                    }
                    serviceConfig.addProperty(ConfigurationConstants.DESTINATION_ELEMENT, destinationConfig);
                }
            }
        }
        return serviceConfig;
    }
