    public void set(StringTokenizer objStrTok, SessionThread objThread, boolean blnCheck) throws Exception {
        String strNewCmd = "";
        String strAddr = null;
        String strDrvMode = null;
        String strVelo = null;
        String strVeloMax = null;
        String strFunc = null;
        int intDummy = -2;
        int intVelo = 0;
        int intVeloMax = 0;
        try {
            strAddr = objStrTok.nextToken().trim();
            strDrvMode = objStrTok.nextToken().trim();
            strVelo = objStrTok.nextToken().trim();
            strVeloMax = objStrTok.nextToken().trim();
        } catch (Exception e) {
            throw new ExcListToShort();
        }
        if (strAddr.equals(Declare.strAddrBroadcast)) {
        } else {
            intDummy = -2;
            try {
                intDummy = Integer.parseInt(strAddr);
            } catch (Exception e) {
            }
            if (intDummy < 0) {
                throw new ExcWrongValue();
            }
        }
        if (strDrvMode.equals(Declare.strDrvUnchanged)) {
        } else {
            intDummy = -2;
            try {
                intDummy = Integer.parseInt(strDrvMode);
            } catch (Exception e) {
            }
            if ((intDummy < 0) || (intDummy > 2)) {
                throw new ExcWrongValue();
            }
        }
        if (((strVelo.equals(Declare.strDrvUnchanged) && !strVeloMax.equals(Declare.strDrvUnchanged))) || ((strVeloMax.equals(Declare.strDrvUnchanged) && !strVelo.equals(Declare.strDrvUnchanged)))) {
            throw new ExcWrongValue();
        }
        if (!strVelo.equals(Declare.strDrvUnchanged)) {
            intVelo = -2;
            try {
                intVelo = Integer.parseInt(strVelo);
            } catch (Exception e) {
            }
            if (intVelo < 0) {
                throw new ExcWrongValue();
            }
        }
        if (!strVeloMax.equals(Declare.strDrvUnchanged)) {
            intVeloMax = -2;
            try {
                intVeloMax = Integer.parseInt(strVeloMax);
            } catch (Exception e) {
            }
            if (intVeloMax < 0) {
                throw new ExcWrongValue();
            }
        }
        if (intVelo > intVeloMax) {
            throw new ExcWrongValue();
        }
        strFunc = "";
        while (objStrTok.hasMoreElements()) {
            strFunc = strFunc + objStrTok.nextToken() + " ";
        }
        getProcessor();
        Operand objOperand = new Operand(strBusNr, Declare.strDevGL, strAddr, strDrvMode, strVelo, strVeloMax, strFunc, objSRCPDaemon.getTimestamp());
        if (!strAddr.equals(Declare.strAddrBroadcast)) {
            if (!objProcessor.isAvailable(objOperand)) {
                throw new ExcUnsupportedDevice();
            }
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
