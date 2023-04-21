    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case NetworkPackage.NETWORK__NODES:
                return getNodes();
            case NetworkPackage.NETWORK__NAME:
                return getName();
            case NetworkPackage.NETWORK__CHANNELS:
                return getChannels();
        }
        return super.eGet(featureID, resolve, coreType);
    }
