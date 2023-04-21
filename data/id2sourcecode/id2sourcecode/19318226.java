    private transferFromIdAut() {
        this.serverUrl = ch.unibe.id.se.a3ublogin.persistence.Constants.getInstance().getIdAutAddress();
        this.baseDN = ch.unibe.id.se.a3ublogin.persistence.Constants.getInstance().getIdAutBaseDN();
        try {
            out = new BufferedWriter(new FileWriter("/groups.txt"));
            outSQL = new BufferedWriter(new FileWriter("/sql.txt"));
        } catch (IOException e) {
            if (log.isWarnEnabled()) {
                log.warn("transferFromIdAut: ", e);
            }
        }
    }
