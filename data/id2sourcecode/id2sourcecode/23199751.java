    private void generateCACert() {
        KeyPair kp = CertificateGenerator.generateKeyPair();
        X509Certificate c;
        if (serverCert != null) generateServerCert();
        try {
            String name = database.getFile().getName();
            name = name.replaceAll(",", "\\,");
            name = name.replaceAll("\\\\", "/");
            c = CertificateGenerator.generateCACertificate(kp, name);
            bindCACertificate(c);
            caCert = c;
            caKey = kp.getPrivate();
            caKeyChanged = true;
        } catch (GeneralSecurityException ex) {
            error(ex.getMessage(), getString("errorCannotGenerateCACert"));
        }
        setCAComponentsVisible(true);
    }
