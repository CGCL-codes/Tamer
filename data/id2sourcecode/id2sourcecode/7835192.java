    public void createBaseProfile(Hashtable<String, String> publicData, String password) {
        if (!eu.popeye.application.PropertiesLoader.isSecurityEnabled()) {
            this.userInformationManager.createBaseProfile(publicData, this.secuComm.generateKeyPair(), password);
        } else {
            KeyPair kp = eu.popeye.security.accesscontrol.CredentialManager.getInstance().getKeys();
            if (kp == null) {
                this.userInformationManager.createBaseProfile(publicData, this.secuComm.generateKeyPair(), password);
            } else {
                this.userInformationManager.createBaseProfile(publicData, kp, password);
            }
        }
    }
