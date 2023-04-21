    public void set(StringTokenizer objStrTok, SessionThread objThread, boolean blnCheck) throws Exception {
        getProcessor();
        String strValue = null;
        try {
            strValue = objStrTok.nextToken();
        } catch (Exception e) {
            throw new ExcListToShort();
        }
        if ((!strValue.equals("ON")) && (!strValue.equals("OFF"))) {
            throw new ExcWrongValue();
        }
        String strFreetext = "";
        while (objStrTok.hasMoreElements()) {
            strFreetext += objStrTok.nextToken() + " ";
        }
        Operand objOperand = new Operand(strBusNr, Declare.strDevPower, strAddrPower, "", strValue, strFreetext, "");
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
