    public static void createKeys(File keyDir) {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            final File file = new File(keyDir, EBICS_JKS);
            if (file.exists()) {
                System.err.println("keys alreaded created");
                return;
            }
            int keysize = 2048;
            String keyAlgName = "RSA";
            String sigAlgName = "SHA1WithRSA";
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            keyGen.initialize(keysize, random);
            KeyPair keypair = keyGen.generateKeyPair();
            PrivateKey privKey = keypair.getPrivate();
            PublicKey pubKey = keypair.getPublic();
            Calendar expiry = Calendar.getInstance();
            expiry.add(Calendar.DAY_OF_YEAR, 5);
            V3TBSCertificateGenerator certGen = new V3TBSCertificateGenerator();
            certGen.setSerialNumber(new DERInteger(BigInteger.valueOf(System.currentTimeMillis())));
            certGen.setIssuer(new X500Name("CN=Test Certificate"));
            X500Name x509Name = new X500Name("CN=ILM Informatique");
            certGen.setSubject(x509Name);
            DERObjectIdentifier sigOID = PKCSObjectIdentifiers.sha1WithRSAEncryption;
            AlgorithmIdentifier sigAlgId = new AlgorithmIdentifier(sigOID, new DERNull());
            certGen.setSignature(sigAlgId);
            certGen.setSubjectPublicKeyInfo(new SubjectPublicKeyInfo((ASN1Sequence) new ASN1InputStream(new ByteArrayInputStream(pubKey.getEncoded())).readObject()));
            certGen.setStartDate(new Time(new Date(System.currentTimeMillis())));
            certGen.setEndDate(new Time(expiry.getTime()));
            TBSCertificateStructure tbsCert = certGen.generateTBSCertificate();
            X509Certificate[] chain = new X509Certificate[1];
            chain[0] = createCert(tbsCert, sigAlgId);
            char[] keyPass = "openconcerto".toCharArray();
            final String alias = "AuthenticationPubKey";
            keyStore.setKeyEntry(alias, privKey, keyPass, chain);
            keyStore.store(new FileOutputStream(file), "openconcerto".toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
