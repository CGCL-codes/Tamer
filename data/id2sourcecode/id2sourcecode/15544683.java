    public void requestPatchDump(int bankNum, int timNum) {
        SysexHandler.NameValue nv[] = new SysexHandler.NameValue[5];
        int timbreAddr = timNum * 0xF6;
        int timSizeH = 0x00;
        int timSizeM = 0x01;
        int timSizeL = 0x76;
        int timAddrH = 0x04;
        int timAddrM = timbreAddr / 0x80;
        int timAddrL = timbreAddr & 0x7F;
        nv[0] = new SysexHandler.NameValue("partAddrM", timAddrM);
        nv[1] = new SysexHandler.NameValue("partAddrL", timAddrL);
        int checkSum = (0 - (timAddrH + timAddrM + timAddrL + timSizeH + timSizeM + timSizeL)) & 0x7F;
        nv[2] = new SysexHandler.NameValue("checkSum", checkSum);
        nv[3] = new SysexHandler.NameValue("bankNum", bankNum << 1);
        nv[4] = new SysexHandler.NameValue("timNum", timNum);
        send(SYS_REQ.toSysexMessage(getChannel(), nv));
    }
