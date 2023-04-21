    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ActionstepPackage.BRIDGE__CALL1:
                if (resolve) return getCall1();
                return basicGetCall1();
            case ActionstepPackage.BRIDGE__CALL2:
                if (resolve) return getCall2();
                return basicGetCall2();
            case ActionstepPackage.BRIDGE__CHANNEL1:
                return getChannel1();
            case ActionstepPackage.BRIDGE__CHANNEL2:
                return getChannel2();
            case ActionstepPackage.BRIDGE__USE_COURTESY_TONE:
                return isUseCourtesyTone();
        }
        return super.eGet(featureID, resolve, coreType);
    }
