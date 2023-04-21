    private synchronized KeyStore readLocalStore(String ksp, String cp, String typ, String provider) throws IOException, GeneralSecurityException, PkiException {
        KeyPair kp;
        X509Certificate cert = null;
        KeyStore ks;
        ks = KeyStore.getInstance(typ, provider);
        File keyStoreFile = new File(ksp);
        File certFile = new File(cp);
        if (keyStoreFile.exists()) {
            ks.load(new FileInputStream(keyStoreFile), password.toCharArray());
            if (certFile.exists()) {
                PEMReader pr = new PEMReader(new FileReader(certFile));
                cert = (X509Certificate) pr.readObject();
                pr.close();
                cert.checkValidity();
                logger.warn("I`m not yet able to check wether " + cp + " is valid. Nor will i send the full certificate chain during a ssl handshake!");
                PublicKey key1 = ks.getCertificate(alias).getPublicKey();
                PublicKey key2 = cert.getPublicKey();
                if (!key1.equals(key2)) {
                    throw new GeneralSecurityException("Keystore key does`t match cert key!");
                }
                Key key = ks.getKey(alias, password.toCharArray());
                ks = saveLocalStore(cert, (PrivateKey) key, ksp, typ, provider);
            }
        } else {
            logger.info("No Keystore found, Generating new one in " + ksp + "...\n" + "Parameters are:\n" + "Key type " + keyType + "\n" + "Key size " + keySize + "\nIsCa=" + isCa);
            ks.load(null, null);
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(keyType);
            keyGen.initialize(Integer.parseInt(keySize));
            kp = keyGen.generateKeyPair();
            v3CertGen.reset();
            v3CertGen.setSerialNumber(BigInteger.valueOf(1));
            v3CertGen.setIssuerDN(new X509Name(dn));
            v3CertGen.setNotBefore(new Date(System.currentTimeMillis()));
            v3CertGen.setNotAfter(new Date(System.currentTimeMillis() + maxValidLong));
            try {
                Date notbefore = Util.getAsDate(Config.getProperty(Config.NOT_BEFORE));
                Date notafter = Util.getAsDate(Config.getProperty(Config.NOT_AFTER));
                v3CertGen.setNotBefore(notbefore);
                v3CertGen.setNotAfter(notafter);
            } catch (Exception e) {
                throw new PkiException("Failed to setup certificate: " + e.getMessage(), e);
            }
            v3CertGen.setSubjectDN(new X509Name(dn));
            v3CertGen.setPublicKey(kp.getPublic());
            v3CertGen.setSignatureAlgorithm(signatureAlgorithm);
            if (isCa) {
                setCaExtensions(kp);
            }
            if (isRa) {
                setRaExtensions(kp);
            }
            cert = v3CertGen.generateX509Certificate((PrivateKey) kp.getPrivate());
            ks = saveLocalStore(cert, kp.getPrivate(), ksp, typ, provider);
            if (certFile.createNewFile()) {
                PEMWriter pr = new PEMWriter(new FileWriter(certFile));
                pr.writeObject(cert);
                pr.close();
            } else {
                logger.error("Unable to create to " + cp);
            }
        }
        return ks;
    }
