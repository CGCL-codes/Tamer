    public static String getFingerPrint(int system) {
        String ret = "";
        String HASH = "";
        switch(system) {
            case -1:
                {
                    byte[] crt = getCertBytes(DefaultCertPath + "server.p12", "12345");
                    md.update(crt);
                    HASH = hexify(md.digest());
                    break;
                }
            case PROLOGUE:
                {
                    if (GlobalSettingsAndNotifier.singleton.getSetting("Prologue_USEDEFAULTCERT").equals("true")) {
                        if (certPrologue == null) {
                            return "N/A";
                        }
                        HASH = hashPrologue;
                        break;
                    } else {
                        byte[] crt = getCertBytes(DefaultCertPath + "server.p12", "12345");
                        md.update(crt);
                        HASH = hexify(md.digest());
                        break;
                    }
                }
            case VOTING:
                {
                    if (GlobalSettingsAndNotifier.singleton.getSetting("Voting_useEmbedded").equals("true")) {
                        if (certVoting == null) {
                            return "N/A";
                        }
                        HASH = hashVoting;
                        break;
                    } else {
                        byte[] crt = getCertBytes(DefaultCertPath + "server.p12", "12345");
                        md.update(crt);
                        HASH = hexify(md.digest());
                    }
                }
        }
        return HASH;
    }
