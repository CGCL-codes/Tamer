    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case AsteriskPackage.CALL__CHANNEL:
                return CHANNEL_EDEFAULT == null ? getChannel() != null : !CHANNEL_EDEFAULT.equals(getChannel());
            case AsteriskPackage.CALL__CALLER_ID_NAME:
                return CALLER_ID_NAME_EDEFAULT == null ? callerIdName != null : !CALLER_ID_NAME_EDEFAULT.equals(callerIdName);
            case AsteriskPackage.CALL__CALLER_ID_NUM:
                return CALLER_ID_NUM_EDEFAULT == null ? callerIdNum != null : !CALLER_ID_NUM_EDEFAULT.equals(callerIdNum);
            case AsteriskPackage.CALL__UNIQUE_ID:
                return UNIQUE_ID_EDEFAULT == null ? uniqueId != null : !UNIQUE_ID_EDEFAULT.equals(uniqueId);
            case AsteriskPackage.CALL__CHANNEL_NAME:
                return CHANNEL_NAME_EDEFAULT == null ? channelName != null : !CHANNEL_NAME_EDEFAULT.equals(channelName);
            case AsteriskPackage.CALL__CALL_STATE:
                return callState != CALL_STATE_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }
