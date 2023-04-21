    public Set<FederationChannel> getChannels(String configurationKey) {
        for (FederationService fc : federationServices) {
            if (fc.getName().equals(configurationKey)) return fc.getOverrideChannels();
        }
        return null;
    }
