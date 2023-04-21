    void receiveDeviceStateReport(UPBMessage theMessage) {
        deviceNetwork.getUPBManager().cancelStateRequest(this);
        if (!deviceNetwork.getUPBManager().getMediaAdapter().isLowLevelAdapter()) {
            if (UPBManager.DEBUG_MODE) debug("Got STATUS report from POST-TRANSFORMED source (not PIM) -- LEVEL=" + theMessage.getLevel() + ", CHAN=" + theMessage.getChannel() + ") -- handing to internal dispatch");
            installNewDeviceLevel(theMessage.getLevel(), theMessage.getFadeRate(), theMessage.getChannel());
            return;
        }
        int forChannel = theMessage.getChannel(), theLevel = theMessage.getLevel();
        boolean isActive = false;
        boolean forceSensors = false, forceOutputs = false;
        boolean changedMap[] = new boolean[ioState.length];
        if (UPBManager.DEBUG_MODE) debug("Got PIM based STATUS report -- LEVEL=" + theLevel + ", CHANNEL=" + forChannel);
        if (forChannel == 1) {
            forceSensors = !sensorsValid;
            for (int sensorIndex = 0; sensorIndex < getSensorCount(); sensorIndex++) {
                isActive = (theLevel & (1 << sensorIndex)) != 0;
                if (ioState[sensorIndex + getOutputCount()] == isActive) continue;
                ioState[sensorIndex + getOutputCount()] = isActive;
                changedMap[sensorIndex + getOutputCount()] = true;
            }
            sensorsValid = true;
        } else if (forChannel == 2) {
            forceOutputs = !outputsValid;
            for (int outputIndex = 0; outputIndex < getOutputCount(); outputIndex++) {
                isActive = (theLevel & (1 << outputIndex)) != 0;
                if (ioState[outputIndex] == isActive) continue;
                ioState[outputIndex] = isActive;
                changedMap[outputIndex] = true;
            }
            outputsValid = true;
        } else if (forChannel == 0) {
            return;
        } else {
            error("Unknown channel report for device (channel " + forChannel + ") -- ignored");
            return;
        }
        for (int ioIndex = 0; ioIndex < getChannelCount(); ioIndex++) {
            if (changedMap[ioIndex] || ((ioIndex < getOutputCount()) && forceOutputs) || ((ioIndex >= getOutputCount() && forceSensors))) {
                deviceStateChanged(ioIndex + 1);
            }
        }
    }
