    public void get(StringTokenizer objStrTok, SessionThread objThread) throws Exception {
        String strNewCmd = "";
        String strAddr = null;
        String strPort = null;
        String strValue = null;
        try {
            strAddr = objStrTok.nextToken();
            strPort = objStrTok.nextToken();
        } catch (Exception e) {
            throw new ExcListToShort();
        }
        getProcessor();
        int intDummy = -2;
        try {
            intDummy = Integer.parseInt(strAddr);
        } catch (Exception e) {
        }
        if (intDummy <= 0) {
            throw new ExcWrongValue();
        }
        intDummy = -2;
        try {
            intDummy = Integer.parseInt(strPort);
        } catch (Exception e) {
        }
        if (intDummy < 0) {
            throw new ExcWrongValue();
        }
        Operand objOperand = new Operand(strBusNr, Declare.strDevGA, strAddr, strPort, "", "", "");
        if (!objProcessor.isAvailable(objOperand)) {
            throw new ExcUnsupportedDevice();
        }
        objOperand = objProcessor.getOperand(objOperand);
        objThread.writeAck(objOperand.getTimestamp(), Declare.intInfoMin, Integer.toString(intBusNr) + " GA " + strAddr + " " + strPort + " " + objOperand.getValue());
    }
