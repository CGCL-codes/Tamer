    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case AsteriskPackage.CALL__CHANNEL:
                return getChannel();
            case AsteriskPackage.CALL__CALLER_ID_NAME:
                return getCallerIdName();
            case AsteriskPackage.CALL__CALLER_ID_NUM:
                return getCallerIdNum();
            case AsteriskPackage.CALL__UNIQUE_ID:
                return getUniqueId();
            case AsteriskPackage.CALL__CHANNEL_NAME:
                return getChannelName();
            case AsteriskPackage.CALL__CALL_STATE:
                return getCallState();
        }
        return super.eGet(featureID, resolve, coreType);
    }
