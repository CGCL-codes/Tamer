    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ActionstepPackage.SOFT_HANGUP__CALL1:
                if (resolve) return getCall1();
                return basicGetCall1();
            case ActionstepPackage.SOFT_HANGUP__HANGUP_ALL_DEVICE_CALLS:
                return isHangupAllDeviceCalls();
            case ActionstepPackage.SOFT_HANGUP__CHANNEL_NAME:
                return getChannelName();
        }
        return super.eGet(featureID, resolve, coreType);
    }
