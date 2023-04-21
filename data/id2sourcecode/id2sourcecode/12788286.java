    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case NetworkPackage.NETWORK__NODES:
                getNodes().clear();
                getNodes().addAll((Collection<? extends Node>) newValue);
                return;
            case NetworkPackage.NETWORK__NAME:
                setName((String) newValue);
                return;
            case NetworkPackage.NETWORK__CHANNELS:
                getChannels().clear();
                getChannels().addAll((Collection<? extends Channel>) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }
