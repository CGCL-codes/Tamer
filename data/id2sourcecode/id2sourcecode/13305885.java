    public void SendOpState() {
        DX7IIParamChanges.xmitVoiceOperatorState((int) (((Driver) (PatchEdit.getDriver(p.deviceNum, p.driverNum))).getPort()), (byte) (0x10 + ((Driver) (PatchEdit.getDriver(p.deviceNum, p.driverNum))).getChannel()), (byte) (OperatorState & 0x3f));
    }
