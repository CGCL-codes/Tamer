    public void wait(StringTokenizer objStrTok, SessionThread objThread) throws Exception {
        String strAddr = null;
        String strValue = null;
        String strTimeout = null;
        try {
            strAddr = objStrTok.nextToken();
            strValue = objStrTok.nextToken();
            strTimeout = objStrTok.nextToken();
        } catch (Exception e) {
            throw new ExcListToShort();
        }
        if (objStrTok.hasMoreElements()) {
            throw new ExcListToLong();
        }
        if ((!strValue.equals("1")) && (!strValue.equals("0"))) {
            throw new ExcWrongValue();
        }
        int intDummy = -2;
        try {
            intDummy = Integer.parseInt(strTimeout);
        } catch (Exception e) {
        }
        if (intDummy <= 0) {
            throw new ExcWrongValue();
        }
        Operand objOperand = new Operand(strBusNr, Declare.strDevFB, strAddr, "", strValue, strTimeout, objSRCPDaemon.getTimestamp());
        getProcessor();
        if (!objProcessor.isAvailable(objOperand)) {
            throw new ExcUnsupportedDevice();
        }
        objProcessor.wait(objOperand);
        objThread.writeAck();
    }
