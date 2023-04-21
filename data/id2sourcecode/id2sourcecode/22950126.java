    public void createEncryptionKeystore() {
        InputStreamReader is = new InputStreamReader(System.in);
        encryptionKeyfile = new File(encryptionKeystoreLocation);
        if (encryptionKeyfile.exists()) {
            sawsPW = getSAWSPasswordOnce(numberOfEncPasswordShares, "encryption", false);
            boolean tempB = checkKeystorePassword(encryptionKeystoreLocation, sawsPW);
            String[] options = { "Create new encryption keystore", "Stop SAWS" };
            int selection = this.createConfirmCallback("The encryption keystore already exists, \nand the password to the encryption keystore is " + tempB + "." + "\n\nOption 1: SAWS will create a new encryption keystore and overwrite the old one. " + "\nOption 2: SAWS will stop.\n", options, SAWSChoiceCallback.WARNING, "ExistingEncKeystore");
            if (selection == 1) {
                this.showMessage("SAWS stoped.", SAWSTextOutputCallback.WARNING);
                System.exit(0);
            } else {
                boolean b1 = encryptionKeyfile.delete();
                if (b1) {
                    this.showMessage(encryptionKeystoreLocation + " has been deleted. ", SAWSTextOutputCallback.INFORMATION);
                } else {
                    this.showMessage(encryptionKeystoreLocation + " can't be deleted. ", SAWSTextOutputCallback.WARNING);
                    System.exit(-1);
                }
            }
        }
        sawsPW = null;
        this.cbs = new Callback[1];
        this.cbs[0] = new CertificateDataCallback(SAWSConstant.ENCRYPTION_PURPOSE);
        try {
            this.callbackHandler.handle(this.cbs);
        } catch (Exception e) {
            sawsDebugLog.write(e);
        }
        CertificateData cd = ((CertificateDataCallback) this.cbs[0]).getCertData();
        if (cd == null) {
            this.showMessage("The process of creating the encryption keystore has been canceled. Keystore was not created.", SAWSTextOutputCallback.WARNING);
        } else {
            sawsPW = getSAWSPasswordOnce(numberOfEncPasswordShares, "encryption", true);
            boolean b1 = createKeystore(encryptionKeystoreLocation, sawsPW, cd);
            if (b1) {
                this.showMessage(encryptionKeystoreLocation + " has been created successfully.", SAWSTextOutputCallback.INFORMATION);
            } else {
                this.showMessage("There is something wrong with creating " + encryptionKeystoreLocation, SAWSTextOutputCallback.WARNING);
            }
        }
    }
