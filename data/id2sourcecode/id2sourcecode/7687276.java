    public void get(StringTokenizer objStrTok, SessionThread objThread) throws Exception {
        getProcessor();
        Operand objOperand = new Operand(strBusNr, Declare.strDevPower, strAddrPower, "", "", "", "");
        if (!objProcessor.isAvailable(objOperand)) {
            throw new ExcUnsupportedDevice();
        }
        String strValue = objProcessor.get(objOperand);
        objThread.writeAck(Declare.intInfoMin, Integer.toString(intBusNr) + " POWER " + strValue);
    }
