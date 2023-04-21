    public void init(StringTokenizer objStrTok, SessionThread objThread) throws Exception {
        String strAddr = null;
        String strProto = null;
        try {
            strAddr = objStrTok.nextToken();
            strProto = objStrTok.nextToken();
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
        if (!strProto.equals(Declare.strProtocolByServer)) {
            throw new ExcUnsupportedProtocol();
        }
        getProcessor();
        String strValue = "";
        while (objStrTok.hasMoreElements()) {
            strValue = strValue + objStrTok.nextToken() + " ";
        }
        Operand objOperand = new Operand(strBusNr, Declare.strDevGL, strAddr, "", strProto + " " + strValue, "", objSRCPDaemon.getTimestamp());
        if (objProcessor.isAvailable(objOperand)) {
            throw new ExcForbidden();
        }
        objOperand.setInit();
        objProcessor.init(objOperand);
        objThread.writeAck();
    }
