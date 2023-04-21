    public void get(StringTokenizer objStrTok, SessionThread objThread) throws Exception {
        String strNewCmd = "";
        String strAddr = null;
        try {
            strAddr = objStrTok.nextToken();
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
        Operand objOperand = new Operand(strBusNr, Declare.strDevGL, strAddr, "", "", "", "");
        if (!objProcessor.isAvailable(objOperand)) {
            throw new ExcUnsupportedDevice();
        }
        objOperand = objProcessor.getOperand(objOperand);
        objThread.writeAck(objOperand.getTimestamp(), Declare.intInfoMin, Integer.toString(intBusNr) + " GL " + strAddr + " " + objOperand.getPort() + " " + objOperand.getValue() + " " + objOperand.getDelay() + " " + objOperand.getFunc());
    }
