    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ActionstepPackage.GET_AVAILABLE_CHANNEL__CALL1:
                if (resolve) return getCall1();
                return basicGetCall1();
            case ActionstepPackage.GET_AVAILABLE_CHANNEL__CHANNELS:
                return getChannels();
            case ActionstepPackage.GET_AVAILABLE_CHANNEL__VARIABLE_NAME:
                return getVariableName();
            case ActionstepPackage.GET_AVAILABLE_CHANNEL__IGNORE_IN_USE:
                return isIgnoreInUse();
            case ActionstepPackage.GET_AVAILABLE_CHANNEL__JUMP_PRIORITY_ON_FAIL:
                return isJumpPriorityOnFail();
        }
        return super.eGet(featureID, resolve, coreType);
    }
