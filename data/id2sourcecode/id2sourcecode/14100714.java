    public void changeGroupCredentialPassword(PasswordInfo passwordInfo, char[] oldPassword, char[] newPassword) throws IOException, GeneralSecurityException {
        if (oldPassword == null) throw new RuntimeException("Old password '" + passwordInfo.getRefId() + "' is null");
        if (!getPasswordHolder().verify(passwordInfo, oldPassword)) throw new RuntimeException("Old Password is invalid for '" + passwordInfo.getRefId() + "'");
        if (newPassword == null) throw new RuntimeException("New password '" + passwordInfo.getRefId() + "' is null");
        CertificateGroupDetail certGroup = getTolvenConfigWrapper().getCredentialGroup(passwordInfo.getRefId());
        CertificateKeyDetail keyDetail = certGroup.getKey();
        PrivateKey privateKey = getPrivateKey(keyDetail, oldPassword);
        File keyFile = new File(keyDetail.getSource());
        KeyStore keyStore = null;
        File keyStoreFile = null;
        CertificateKeyStoreDetail certKeyStoreDetail = certGroup.getKeyStore();
        if (certKeyStoreDetail != null) {
            keyStore = getTolvenConfigWrapper().getKeyStore(oldPassword, certKeyStoreDetail);
            keyStoreFile = new File(certKeyStoreDetail.getSource());
        }
        TrustStoreDetail trustStoreDetail = getTolvenConfigWrapper().getTrustStoreDetail(passwordInfo.getRefId());
        KeyStore trustStore = null;
        File trustStoreFile = null;
        if (trustStore != null) {
            trustStore = getTolvenConfigWrapper().getTrustStore(oldPassword, trustStoreDetail);
            trustStoreFile = new File(trustStoreDetail.getSource());
        }
        File tmpKey = null;
        File tmpKeyStore = null;
        File tmpTrustStore = null;
        boolean success = false;
        try {
            getTolvenConfigWrapper().getBuildDir().mkdirs();
            tmpKey = new File(getTolvenConfigWrapper().getBuildDir(), keyFile.getName());
            write(privateKey, keyDetail.getFormat(), tmpKey, newPassword);
            if (keyStoreFile != null) {
                tmpKeyStore = new File(getTolvenConfigWrapper().getBuildDir(), keyStoreFile.getName());
                String alias = keyStore.aliases().nextElement();
                Key key = keyStore.getKey(alias, oldPassword);
                Certificate[] chain = keyStore.getCertificateChain(alias);
                keyStore.setKeyEntry(alias, key, newPassword, chain);
                write(keyStore, tmpKeyStore, newPassword);
            }
            if (trustStoreFile != null) {
                tmpTrustStore = new File(getTolvenConfigWrapper().getBuildDir(), trustStoreFile.getName());
                write(trustStore, tmpTrustStore, newPassword);
            }
            FileUtils.copyFile(tmpKey, keyFile);
            if (keyStoreFile != null) {
                FileUtils.copyFile(tmpKeyStore, keyStoreFile);
            }
            if (trustStoreFile != null) {
                FileUtils.copyFile(tmpTrustStore, trustStoreFile);
            }
            success = true;
        } finally {
            if (success) {
                if (tmpKey != null) {
                    tmpKey.delete();
                }
                if (tmpKeyStore != null) {
                    tmpKeyStore.delete();
                }
                if (tmpKeyStore != null) {
                    tmpKeyStore.delete();
                }
                getPasswordHolder().changePassword(passwordInfo, oldPassword, newPassword);
            }
        }
    }
