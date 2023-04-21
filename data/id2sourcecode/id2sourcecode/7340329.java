    public void reset(StringTokenizer objStrTok, SessionThread objThread) throws Exception {
        String strAddr = null;
        String strDummy = null;
        try {
            strAddr = objStrTok.nextToken();
            strDummy = objStrTok.nextToken();
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
        String strInfo = info(strAddr);
        Operand objOperand = new Operand(strBusNr, Declare.strDevGA, strAddr, "", "", "", objSRCPDaemon.getTimestamp());
        if (!objProcessor.isAvailable(objOperand)) {
            throw new ExcUnsupportedDevice();
        }
        objOperand.setTerm();
        objProcessor.term(objOperand);
        objOperand = new Operand(strBusNr, Declare.strDevGA, strAddr, "", strInfo, "", objSRCPDaemon.getTimestamp());
        objOperand.setInit();
        objProcessor.init(objOperand);
        objThread.writeAck();
    }
