    public void set(StringTokenizer objStrTok, SessionThread objThread, boolean blnCheck) throws Exception {
        String strNewCmd = "";
        String strAddr = null;
        String strPort = null;
        String strValue = null;
        String strDelay = null;
        try {
            strAddr = objStrTok.nextToken().trim();
            strPort = objStrTok.nextToken().trim();
            strValue = objStrTok.nextToken().trim();
            strDelay = objStrTok.nextToken().trim();
        } catch (Exception e) {
            throw new ExcListToShort();
        }
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
        intDummy = -2;
        try {
            intDummy = Integer.parseInt(strValue);
        } catch (Exception e) {
        }
        if (intDummy < 0) {
            throw new ExcWrongValue();
        }
        intDummy = -2;
        try {
            intDummy = Integer.parseInt(strDelay);
        } catch (Exception e) {
        }
        if ((intDummy < -1) || (intDummy == 0)) {
            throw new ExcWrongValue();
        }
        getProcessor();
        Operand objOperand = new Operand(strBusNr, Declare.strDevGA, strAddr, strPort, strValue, strDelay, objSRCPDaemon.getTimestamp());
        if (!objProcessor.isAvailable(objOperand)) {
            throw new ExcUnsupportedDevice();
        }
        if (!blnCheck) {
            try {
                objProcessor.set(objOperand);
            } catch (Exception e) {
                throw new ExcProcessorDied();
            }
        }
        objThread.writeAck();
    }
