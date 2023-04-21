    protected void stopUnit(OsgiIdentityMediationUnit unit) throws IdentityMediationException {
        Collection<Channel> channels = unit.getChannels();
        for (Channel channel : channels) {
            if (channel.getUnitContainer() != null) {
                IdentityMediationUnitContainer unitContainer = channel.getUnitContainer();
                unitContainer.stop();
            }
        }
    }
