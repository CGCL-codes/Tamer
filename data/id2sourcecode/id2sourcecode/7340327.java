    public void init(StringTokenizer objStrTok, SessionThread objThread) throws Exception {
        String strAddr = null;
        String strProto = null;
        try {
            strAddr = objStrTok.nextToken();
            strProto = objStrTok.nextToken();
        } catch (Exception e) {
            throw new ExcListToShort();
        }
        int intAddr = Integer.parseInt(strAddr);
        if (strProto.equals(Declare.strProtocolMaerklinMotorola)) {
            if ((intAddr < 1) || (intAddr > 256)) {
                throw new ExcWrongValue();
            }
        } else {
            if (strProto.equals(Declare.strProtocolNmraDcc)) {
                if ((intAddr < 1) || (intAddr > 511)) {
                    throw new ExcWrongValue();
                }
            } else {
                if (strProto.equals(Declare.strProtocolByServer)) {
                    if (intAddr < 1) {
                        throw new ExcWrongValue();
                    }
                } else {
                    throw new ExcWrongValue();
                }
            }
        }
        getProcessor();
        String strValue = "";
        while (objStrTok.hasMoreElements()) {
            strValue = strValue + objStrTok.nextToken() + " ";
        }
        Operand objOperand = new Operand(strBusNr, Declare.strDevGA, strAddr, "", strProto + " " + strValue, "", objSRCPDaemon.getTimestamp());
        if (objProcessor.isAvailable(objOperand)) {
            throw new ExcForbidden();
        }
        objOperand.setInit();
        objProcessor.init(objOperand);
        objThread.writeAck();
    }
