    public void TFSNRWFileGo() {
        Logger logger = new Logger("Main Class");
        String customMsg = "TFSNRWFile ";
        for (int x = 0; x < TFSNData.banned.length; x++) {
            logger.fatal(customMsg.toString() + "Defining banned array".toString());
            TFSNData.banned[x] = "myname";
        }
        for (int x = 0; x < TFSNData.remotebanned.length; x++) {
            logger.fatal(customMsg.toString() + "Defining remote banned array".toString());
            TFSNData.remotebanned[x] = "myname";
        }
        TSN = new Thread(this, "TFSN Readwritefile Description");
        TSN.start();
    }
