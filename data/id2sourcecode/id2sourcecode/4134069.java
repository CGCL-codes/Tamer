    public boolean doAsynchronousAction() throws GeneralSecurityException {
        KeyPair keyPair = null;
        if (isValidInput()) {
            FileInputStream fis = null;
            try {
                java.security.cert.Certificate cert = null;
                if (getCertificateAbsolutePath() != null) {
                    File certFile = new File(getCertificateAbsolutePath());
                    if (!certFile.exists()) {
                        throw new KeyException("Cannot open non-existent certificate file: " + getCertificateAbsolutePath());
                    }
                    fis = new FileInputStream(certFile);
                    CertificateFactory certFact = CertificateFactory.getInstance(getCertificateEncoding());
                    cert = certFact.generateCertificate(fis);
                }
                KeyPairGenerator keyGen = KeyPairGenerator.getInstance(getAlgorithm(), getProvider());
                keyGen.initialize(getSize());
                keyPair = keyGen.generateKeyPair();
                KeyPairEvent evt = new KeyPairEvent(this, KeyEvent.CREATE, keyPair, getAlias(), getPassword(), getPublicKeyAbsolutePath(), cert);
                publishEvent(getKeyPairEventListeners(), evt);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace(System.err);
                System.exit(-1);
            } catch (CertificateException e) {
                e.printStackTrace(System.err);
                System.exit(-1);
            } catch (IOException e) {
                e.printStackTrace(System.err);
                System.exit(-1);
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                        fis = null;
                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                    }
                }
            }
            return true;
        }
        return false;
    }
