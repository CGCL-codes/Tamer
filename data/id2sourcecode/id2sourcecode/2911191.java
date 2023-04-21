    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ActionstepPackage.CHAN_SPY__CALL1:
                if (resolve) return getCall1();
                return basicGetCall1();
            case ActionstepPackage.CHAN_SPY__CHANNELNAME_PREFIX:
                return getChannelnamePrefix();
            case ActionstepPackage.CHAN_SPY__SPY_BRIDGED_ONLY:
                return isSpyBridgedOnly();
            case ActionstepPackage.CHAN_SPY__GROUP:
                return getGroup();
            case ActionstepPackage.CHAN_SPY__BEEP:
                return isBeep();
            case ActionstepPackage.CHAN_SPY__RECORD_FILENAME_PREFIX:
                return getRecordFilenamePrefix();
            case ActionstepPackage.CHAN_SPY__VOLUME:
                return getVolume();
            case ActionstepPackage.CHAN_SPY__WHISPER_ENABLED:
                return isWhisperEnabled();
            case ActionstepPackage.CHAN_SPY__PRIVATE_WHISPER_ENABLED:
                return isPrivateWhisperEnabled();
        }
        return super.eGet(featureID, resolve, coreType);
    }
