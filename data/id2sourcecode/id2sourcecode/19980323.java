    public void get(StringTokenizer objStrTok, SessionThread objThread) throws Exception {
        String strNewCmd = "";
        String strAddr = null;
        String strValue = null;
        try {
            strAddr = objStrTok.nextToken();
        } catch (Exception e) {
            throw new ExcListToShort();
        }
        if (objStrTok.hasMoreElements()) {
            throw new ExcListToLong();
        }
        getProcessor();
        int intAddr = -2;
        try {
            intAddr = Integer.parseInt(strAddr);
        } catch (Exception e) {
        }
        if (intAddr <= 0) {
            throw new ExcWrongValue();
        }
        Operand objOperand = new Operand(strBusNr, Declare.strDevFB, strAddr, "", "", "", "");
        if (!objProcessor.isAvailable(objOperand)) {
            throw new ExcUnsupportedDevice();
        }
        strValue = objProcessor.get(objOperand);
        objThread.writeAck(Declare.intInfoMin, Integer.toString(intBusNr) + " FB " + strAddr + " " + strValue);
    }
