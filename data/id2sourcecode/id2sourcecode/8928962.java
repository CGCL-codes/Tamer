    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ActionstepPackage.PICKUP_CHAN__CALL1:
                if (resolve) return getCall1();
                return basicGetCall1();
            case ActionstepPackage.PICKUP_CHAN__CHANNELS:
                return getChannels();
        }
        return super.eGet(featureID, resolve, coreType);
    }
