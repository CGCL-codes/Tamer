    public void run() {
        if (wioControler == null || sigManager == null || urlTarget == null) {
            panel.writeLine("[!] Bad initialization, QUITTING!!");
            return;
        }
        panel.writeLine("[+] Running...");
        try {
            HttpURLConnection conn = (HttpURLConnection) urlTarget.openConnection();
            conn.connect();
        } catch (ConnectException e) {
            panel.writeLine("[!] Can't open connection (" + e.getMessage() + ")");
            panel.writeLine("[!] Aborted!");
            return;
        } catch (IOException e) {
            panel.writeLine("[!] Unknown target (" + e.getMessage() + ")");
            panel.writeLine("[!] Aborted!");
            return;
        }
        appSig.addChallenge(new ChallengeFaviconMd5(urlTarget), 50);
        appSig.addChallenge(new ChallengeContainedLinks(urlTarget), 50);
        appSig.performTests();
        panel.writeLine("====== SIGNATURE ======");
        panel.writeLine(appSig.computeResult());
        panel.writeLine("=======================");
        Pair<String, String> baseSig = sigManager.getClosestSignature(appSig);
        if (baseSig != null) {
            panel.writeLine("[+] The application is " + baseSig.getA() + ", version :" + baseSig.getB());
        } else {
            String appVersion = null;
            String appName = WarssInputDialog.getWarssInputNameVersion("Application name :");
            if (appName != null) {
                appVersion = WarssInputDialog.getWarssInputNameVersion("Application version :");
            }
            sigManager.addSig(appSig, appName, appVersion);
            sigManager.saveFile(sigManager.getFilename());
            panel.writeLine("[+] The application is " + appName + ", version :" + appVersion);
        }
        panel.writeLine("[+] DEBUG : scan terminated");
    }
