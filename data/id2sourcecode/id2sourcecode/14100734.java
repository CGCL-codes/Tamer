    public void processCredentialGroup(CertificateGroupDetail certGroup, char[] password) throws IOException, GeneralSecurityException {
        CertificateDetail cert = certGroup.getCertificate();
        CertificateKeyDetail key = certGroup.getKey();
        if ((key == null && cert != null) || (key != null && cert == null)) {
            throw new RuntimeException("Group: " + certGroup.getId() + " must either have a both key and certificate definition or neither");
        }
        if (key != null && cert != null) {
            File certFile = new File(cert.getSource());
            File keyFile = new File(key.getSource());
            if (key.isCommercial()) {
                if (!(certFile.exists() && keyFile.exists())) throw new RuntimeException(certGroup.getId() + " is a commercial group. " + certFile.getPath() + " and " + keyFile.getPath() + " must exist");
            } else {
                if (!certFile.exists() && !keyFile.exists()) {
                    logger.info("New credentials will be created for certificate " + cert.getId());
                    getTolvenConfigWrapper().getBuildDir().mkdirs();
                    X500Principal x500Principal = getX500Principal(certGroup);
                    X509CertificatePrivateKeyPair certPrivateKeyPair = createSelfSignedCertificate(x500Principal);
                    X509Certificate certificate = null;
                    if (cert.getId().equals(cert.getCaRefId())) {
                        certificate = certPrivateKeyPair.getX509Certificate();
                    } else {
                        certificate = signCertificate(cert, x500Principal, certPrivateKeyPair.getX509Certificate().getPublicKey());
                    }
                    File tmpCertFile = null;
                    File tmpKeyFile = null;
                    try {
                        tmpCertFile = File.createTempFile("cert", ".tmp", getTolvenConfigWrapper().getBuildDir());
                        tmpCertFile.delete();
                        tmpKeyFile = File.createTempFile("key", ".tmp", getTolvenConfigWrapper().getBuildDir());
                        tmpKeyFile.delete();
                        char[] keypass = password;
                        if (key.isPasswordProtected()) {
                            if (keypass == null) {
                                keypass = getPasswordHolder().getPassword(certGroup.getId());
                                if (keypass == null) {
                                    throw new RuntimeException("Password for key in group " + certGroup.getId() + " cannot be null");
                                }
                            }
                            if (getPasswordHolder().getPassword(certGroup.getId()) == null) {
                                getPasswordHolder().addPassword(certGroup.getId(), PasswordHolder.CREDENTIAL_PASSWORD, keypass);
                            }
                        } else {
                            if (password != null) {
                                throw new RuntimeException("The key for group " + certGroup.getId() + " is not protected, yet a password was supplied");
                            }
                        }
                        PrivateKey privateKey = certPrivateKeyPair.getPrivateKey();
                        if (TolvenConfigWrapper.TOLVEN_CREDENTIAL_FORMAT_PEM.equals(key.getFormat())) {
                            writePEMObject(certificate, tmpCertFile);
                            writePEMObject(keypass, privateKey, tmpKeyFile);
                        } else {
                            writeDER(certificate, tmpCertFile);
                            writeDER(keypass, privateKey, tmpKeyFile);
                        }
                        FileUtils.copyFile(tmpCertFile, certFile);
                        FileUtils.copyFile(tmpKeyFile, keyFile);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        if (certFile != null) certFile.delete();
                        if (keyFile != null) keyFile.delete();
                        throw new RuntimeException("Could not create key and certificate for group: " + certGroup.getId(), ex);
                    } finally {
                        if (tmpCertFile != null) tmpCertFile.delete();
                        if (tmpKeyFile != null) tmpKeyFile.delete();
                    }
                } else {
                    if (!(certFile.exists() && keyFile.exists())) throw new RuntimeException(certGroup.getId() + " is a non-commercial group with existing credentials. " + certFile.getPath() + " and " + keyFile.getPath() + " must exist");
                }
            }
        }
        CertificateKeyStoreDetail certificateKeyStoreDetail = certGroup.getKeyStore();
        if (certificateKeyStoreDetail != null && !new File(certificateKeyStoreDetail.getSource()).exists()) {
            logger.info("New keyStore will be created: " + certificateKeyStoreDetail.getId() + " at " + certificateKeyStoreDetail.getSource());
            createKeyStore(certGroup, password);
        }
    }
