    public void term(StringTokenizer objStrTok, SessionThread objThread) throws Exception {
        if (objStrTok.hasMoreElements()) {
            throw new ExcListToLong();
        }
        objThread.writeAck();
        objThread.getSRCPDaemon().stopServer();
    }
