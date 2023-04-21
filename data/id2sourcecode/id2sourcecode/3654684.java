    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ActionstepPackage.EXTENSION_SPY__CALL1:
                if (resolve) return getCall1();
                return basicGetCall1();
            case ActionstepPackage.EXTENSION_SPY__EXTENSION:
                return getExtension();
            case ActionstepPackage.EXTENSION_SPY__CONTEXT:
                return getContext();
            case ActionstepPackage.EXTENSION_SPY__SPY_BRIDGED_ONLY:
                return isSpyBridgedOnly();
            case ActionstepPackage.EXTENSION_SPY__GROUP:
                return getGroup();
            case ActionstepPackage.EXTENSION_SPY__BEEP:
                return isBeep();
            case ActionstepPackage.EXTENSION_SPY__RECORD_FILENAME_PREFIX:
                return getRecordFilenamePrefix();
            case ActionstepPackage.EXTENSION_SPY__VOLUME:
                return getVolume();
            case ActionstepPackage.EXTENSION_SPY__WHISPER_ENABLED:
                return isWhisperEnabled();
            case ActionstepPackage.EXTENSION_SPY__PRIVATE_WHISPER_ENABLED:
                return isPrivateWhisperEnabled();
            case ActionstepPackage.EXTENSION_SPY__CHANNEL_NAME:
                return getChannelName();
        }
        return super.eGet(featureID, resolve, coreType);
    }
