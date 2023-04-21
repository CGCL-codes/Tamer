    public void requestPatchDump(int bankNum, int patchNum) {
        System.out.println("BANKNUM: " + bankNum + "PATCHNUM: " + patchNum);
        send(SYS_REQ.toSysexMessage(getChannel(), new SysexHandler.NameValue("bankNum", TCElectronicGMajorUtil.calcBankNum(bankNum, patchNum)), new SysexHandler.NameValue("patchNum", TCElectronicGMajorUtil.calcPatchNum(bankNum, patchNum))));
    }
