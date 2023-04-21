    public void init(StringTokenizer objStrTok, SessionThread objThread) throws Exception {
        Operand objOperand = new Operand(strBusNr, Declare.strDevPower, strAddrPower, "", "", "", objSRCPDaemon.getTimestamp());
        objOperand.setInit();
        getProcessor();
        objProcessor.init(objOperand);
        objThread.writeAck();
    }
