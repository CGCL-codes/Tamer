    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case NetworkPackage.NETWORK__NODES:
                getNodes().clear();
                return;
            case NetworkPackage.NETWORK__NAME:
                setName(NAME_EDEFAULT);
                return;
            case NetworkPackage.NETWORK__CHANNELS:
                getChannels().clear();
                return;
        }
        super.eUnset(featureID);
    }
