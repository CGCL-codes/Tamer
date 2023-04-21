    private static void loadInfo(KeyStore ks, int system, String passphrase, File file) {
        switch(system) {
            case PROLOGUE:
                {
                    try {
                        certPrologue = ks.getCertificate(ks.aliases().nextElement()).toString();
                        md.update(ks.getCertificate(ks.aliases().nextElement()).getEncoded());
                        hashPrologue = hexify(md.digest());
                        GlobalSettingsAndNotifier.singleton.modifySettings("Prologue_certpath", file.getAbsolutePath(), true);
                        PrologueServer.setCertPass(passphrase);
                        break;
                    } catch (CertificateEncodingException ex) {
                        new ErrorDialog(null, true, GlobalSettingsAndNotifier.singleton.messages.getString("certFail") + "\nCertManager\n" + ex.toString(), false).setVisible(true);
                    } catch (KeyStoreException ex) {
                        new ErrorDialog(null, true, GlobalSettingsAndNotifier.singleton.messages.getString("FATAL") + "\nCertManager\n" + ex.toString(), true).setVisible(true);
                    }
                }
            case VOTING:
                {
                    try {
                        certVoting = ks.getCertificate(ks.aliases().nextElement()).toString();
                        md.update(ks.getCertificate(ks.aliases().nextElement()).getEncoded());
                        hashVoting = hexify(md.digest());
                        GlobalSettingsAndNotifier.singleton.modifySettings("Voting_certpath", file.getAbsolutePath(), true);
                        Server.setCertPass(passphrase);
                        break;
                    } catch (CertificateEncodingException ex) {
                        new ErrorDialog(null, true, GlobalSettingsAndNotifier.singleton.messages.getString("certFail") + "\nCertManager\n" + ex.toString(), false).setVisible(true);
                    } catch (KeyStoreException ex) {
                        new ErrorDialog(null, true, GlobalSettingsAndNotifier.singleton.messages.getString("FATAL") + "\nCertManager\n" + ex.toString(), true).setVisible(true);
                    }
                }
        }
    }
