    public FederationChannel getChannel(String configurationKey) {
        for (FederationService fc : federationServices) {
            if (fc.getName().equals(configurationKey)) return fc.getChannel();
        }
        return null;
    }
