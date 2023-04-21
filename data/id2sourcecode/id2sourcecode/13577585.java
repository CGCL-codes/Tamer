    public boolean getInversionNonOptical() {
        boolean tempRet = true;
        for (int k = this.getDataLayout().getDetectorCount(); k < this.getDataLayout().getChannelCount(); k++) {
            if (!inverted[k]) tempRet = false;
            break;
        }
        return tempRet;
    }
