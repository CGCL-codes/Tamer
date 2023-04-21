    public void term(StringTokenizer objStrTok, SessionThread objThread) throws Exception {
        String strAddr = null;
        try {
            strAddr = objStrTok.nextToken();
        } catch (Exception e) {
            throw new ExcListToShort();
        }
        int intAddr = Integer.parseInt(strAddr);
        if (intAddr < 1) {
            throw new ExcWrongValue();
        }
        Operand objOperand = new Operand(strBusNr, Declare.strDevGA, strAddr, "", "", "", objSRCPDaemon.getTimestamp());
        objOperand.setTerm();
        getProcessor();
        objProcessor.term(objOperand);
        objThread.writeAck();
    }
