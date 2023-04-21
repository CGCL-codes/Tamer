    public Set<FederationChannel> getChannels() {
        if (defaultFederationService == null) {
            return null;
        }
        return this.defaultFederationService.getOverrideChannels();
    }
