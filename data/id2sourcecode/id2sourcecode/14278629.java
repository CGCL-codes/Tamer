    public void get(StringTokenizer objStrTok, SessionThread objThread) throws Exception {
        if (objStrTok.hasMoreElements()) {
            throw new ExcListToLong();
        }
        String strState = "";
        switch(objThread.getSRCPDaemon().getServerState()) {
            case SRCPDaemon.intStateRunning:
                strState = Declare.strStateRunning;
                break;
            case SRCPDaemon.intStateTerminating:
                strState = Declare.strStateTerminating;
                break;
            case SRCPDaemon.intStateResetting:
                strState = Declare.strStateResetting;
                break;
        }
        objThread.writeAck(Declare.intInfoMin, strGroupIdentifier + " " + strState);
    }
