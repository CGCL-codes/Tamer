    public void createSigningKeystore() {
        InputStreamReader is = new InputStreamReader(System.in);
        signingKeyfile = new File(signingKeystoreLocation);
        if (signingKeyfile.exists()) {
            sawsPW = getSAWSPasswordOnce(numberOfPasswordShares, "signing", false);
            boolean tempB = checkKeystorePassword(signingKeystoreLocation, sawsPW);
            String[] options = { "Create new signing keystore", "Stop SAWS" };
            int selection = this.createConfirmCallback("The signing keystore already exists, \nand the password to the signing keystore is " + tempB + "." + "\n\nOption 1: SAWS will create a new signing keystore and overwrite the old one. " + "\nOption 2: SAWS will stop.\n", options, SAWSChoiceCallback.WARNING, "ExistingSigKeystore");
            if (selection == 1) {
                this.showMessage("SAWS stoped.", SAWSTextOutputCallback.WARNING);
                System.exit(0);
            } else {
                boolean b1 = signingKeyfile.delete();
                if (b1) {
                    this.showMessage(signingKeystoreLocation + " has been deleted. ", SAWSTextOutputCallback.INFORMATION);
                } else {
                    this.showMessage(signingKeystoreLocation + " can't be deleted. ", SAWSTextOutputCallback.WARNING);
                    System.exit(-1);
                }
            }
        }
        sawsPW = null;
        this.cbs = new Callback[1];
        this.cbs[0] = new CertificateDataCallback(SAWSConstant.SIGNING_PURPOSE);
        try {
            this.callbackHandler.handle(this.cbs);
        } catch (Exception e) {
            sawsDebugLog.write(e);
        }
        CertificateData cd = ((CertificateDataCallback) this.cbs[0]).getCertData();
        if (cd == null) {
            this.showMessage("The process of creating the signing keystore has been canceled. Keystore was not created.", SAWSTextOutputCallback.WARNING);
        } else {
            sawsPW = getSAWSPasswordOnce(numberOfPasswordShares, "signing", true);
            boolean b1 = createKeystore(signingKeystoreLocation, sawsPW, cd);
            if (b1) {
                this.showMessage(signingKeystoreLocation + " has been created successfully.", SAWSTextOutputCallback.INFORMATION);
            } else {
                this.showMessage("There is something wrong with creating " + signingKeystoreLocation, SAWSTextOutputCallback.WARNING);
            }
        }
    }
