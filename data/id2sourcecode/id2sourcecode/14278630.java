    public void reset(StringTokenizer objStrTok, SessionThread objThread) throws Exception {
        if (objStrTok.hasMoreElements()) {
            throw new ExcListToLong();
        }
        objThread.getSRCPDaemon().setState(SRCPDaemon.intStateResetting);
        objThread.writeAck();
    }
