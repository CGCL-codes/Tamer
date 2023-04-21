    public void term(StringTokenizer objStrTok, SessionThread objThread) throws Exception {
        if (objStrTok.hasMoreElements()) {
            throw new ExcListToLong();
        }
        getProcessor();
        Operand objOperand = new Operand(strBusNr, Declare.strDevFB, "1", "", "", "", objSRCPDaemon.getTimestamp());
        objOperand.setTerm();
        if (!objProcessor.isAvailable(objOperand)) {
            throw new ExcUnsupportedDevice();
        }
        objProcessor.term(objOperand);
        objThread.writeAck();
    }
