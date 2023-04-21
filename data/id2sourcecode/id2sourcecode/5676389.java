    @Test
    public void saveAndLoadCAPair() throws Exception {
        runWithNewDatabase(new DatabaseRunnable() {

            public void run(DatabaseFile db) throws IOException, StaleException {
                KeyPair kp = CertificateGenerator.generateKeyPair();
                X509Certificate cert;
                try {
                    cert = CertificateGenerator.generateCACertificate(kp, db.getFile().getName());
                } catch (GeneralSecurityException e) {
                    throw new IllegalStateException(e);
                }
                db.setCAKeyPair(cert, kp.getPrivate());
                db.save();
                db.open(TEST_PASSWORD.clone());
                Assert.assertEquals(cert, db.getCAKeyPair().getKey());
            }
        });
    }
