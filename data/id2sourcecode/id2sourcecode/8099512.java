    public void term(StringTokenizer objStrTok, SessionThread objThread) throws Exception {
        String strAddr = null;
        try {
            strAddr = objStrTok.nextToken();
        } catch (Exception e) {
            throw new ExcListToShort();
        }
        int intAddr = Integer.parseInt(strAddr);
        if (intAddr < 1) {
            throw new ExcWrongValue();
        }
        getProcessor();
        Operand objOperand = new Operand(strBusNr, Declare.strDevGL, strAddr, "", "", "", objSRCPDaemon.getTimestamp());
        if (!objProcessor.isAvailable(objOperand)) {
            throw new ExcUnsupportedDevice();
        }
        objOperand.setTerm();
        getProcessor();
        objProcessor.term(objOperand);
        objThread.writeAck();
    }
