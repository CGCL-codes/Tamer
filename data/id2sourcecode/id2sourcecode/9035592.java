    public void requestPatchDump(int bankNum, int patNum) {
        int patchAddr = patNum * 0x08;
        int patAddrH = 0x05;
        int patAddrM = patchAddr / 0x80;
        int patAddrL = patchAddr & 0x7F;
        int patSizeH = 0x00;
        int patSizeM = 0x00;
        int patSizeL = 0x07;
        int checkSum = (0 - (patAddrH + patAddrM + patAddrL + patSizeH + patSizeM + patSizeL)) & 0x7F;
        SysexHandler.NameValue nVs[] = new SysexHandler.NameValue[3];
        nVs[0] = new SysexHandler.NameValue("partAddrM", patAddrM);
        nVs[1] = new SysexHandler.NameValue("partAddrL", patAddrL);
        nVs[2] = new SysexHandler.NameValue("checkSum", checkSum);
        send(SYS_REQ.toSysexMessage(getChannel(), nVs));
    }
