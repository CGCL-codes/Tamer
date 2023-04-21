    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ActionstepPackage.EXTENSION_TRANSFER__CALL1:
                if (resolve) return getCall1();
                return basicGetCall1();
            case ActionstepPackage.EXTENSION_TRANSFER__CALL2:
                if (resolve) return getCall2();
                return basicGetCall2();
            case ActionstepPackage.EXTENSION_TRANSFER__CONTEXT:
                return getContext();
            case ActionstepPackage.EXTENSION_TRANSFER__EXTENSION:
                return getExtension();
            case ActionstepPackage.EXTENSION_TRANSFER__PRIORITY:
                return getPriority();
            case ActionstepPackage.EXTENSION_TRANSFER__TIMEOUT:
                return getTimeout();
            case ActionstepPackage.EXTENSION_TRANSFER__OPTIONS:
                return getOptions();
            case ActionstepPackage.EXTENSION_TRANSFER__DO_PRE_EXTEN_STATUS_CHECK:
                return isDoPreExtenStatusCheck();
            case ActionstepPackage.EXTENSION_TRANSFER__CHANNEL_TYPE:
                return getChannelType();
        }
        return super.eGet(featureID, resolve, coreType);
    }
