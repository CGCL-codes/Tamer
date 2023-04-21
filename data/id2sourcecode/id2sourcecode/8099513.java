    public void reset(StringTokenizer objStrTok, SessionThread objThread) throws Exception {
        String strAddr = null;
        try {
            strAddr = objStrTok.nextToken();
        } catch (Exception e) {
            throw new ExcListToShort();
        }
        int intAddr = -2;
        try {
            intAddr = Integer.parseInt(strAddr);
        } catch (Exception e) {
        }
        if (intAddr < 1) {
            throw new ExcWrongValue();
        }
        getProcessor();
        Operand objOperand = new Operand(strBusNr, Declare.strDevGL, strAddr, "", "", "", objSRCPDaemon.getTimestamp());
        if (!objProcessor.isAvailable(objOperand)) {
            throw new ExcUnsupportedDevice();
        }
        String strInfo = info(strAddr);
        objOperand.setTerm();
        objProcessor.term(objOperand);
        objOperand = new Operand(strBusNr, Declare.strDevGL, strAddr, "", strInfo, "", objSRCPDaemon.getTimestamp());
        objOperand.setInit();
        objProcessor.init(objOperand);
        objThread.writeAck();
    }
