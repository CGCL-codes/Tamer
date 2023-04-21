    public void reset(StringTokenizer objStrTok, SessionThread objThread) throws Exception {
        if (objStrTok.hasMoreElements()) {
            throw new ExcListToLong();
        }
        String strInfo = info("0");
        Operand objOperand = new Operand(strBusNr, Declare.strDevFB, "1", "", "", "", objSRCPDaemon.getTimestamp());
        if (!objProcessor.isAvailable(objOperand)) {
            throw new ExcUnsupportedDevice();
        }
        objOperand.setTerm();
        objProcessor.term(objOperand);
        objOperand = new Operand(strBusNr, Declare.strDevFB, "1", "", strInfo, "", objSRCPDaemon.getTimestamp());
        objOperand.setInit();
        objProcessor.init(objOperand);
        objThread.writeAck();
    }
