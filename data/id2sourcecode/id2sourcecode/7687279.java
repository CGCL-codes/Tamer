    public void term(StringTokenizer objStrTok, SessionThread objThread) throws Exception {
        Operand objOperand = new Operand(strBusNr, Declare.strDevPower, strAddrPower, "", "", "", objSRCPDaemon.getTimestamp());
        objOperand.setTerm();
        getProcessor();
        objProcessor.term(objOperand);
        objThread.writeAck();
    }
