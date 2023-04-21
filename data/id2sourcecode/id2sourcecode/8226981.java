    protected boolean createCertificate(String commonName, int validityDays, String exportFile, String exportPassword, String exportAlias) throws IOException, InvalidKeyException, SecurityException, SignatureException, NoSuchAlgorithmException, DataLengthException, CryptoException, KeyStoreException, CertificateException, InvalidKeySpecException {
        if (commonName == null || exportFile == null || exportPassword == null || validityDays < 1) {
            throw new IllegalArgumentException("Can not work with null parameter");
        }
        System.out.println("Generating certificate for distinguished common subject name '" + commonName + "', valid for " + validityDays + " days");
        SecureRandom sr = new SecureRandom();
        PublicKey pubKey;
        PrivateKey privKey;
        RSAPrivateCrtKeyParameters privateKey = null;
        System.out.println("Creating RSA keypair");
        RSAKeyPairGenerator gen = new RSAKeyPairGenerator();
        gen.init(new RSAKeyGenerationParameters(BigInteger.valueOf(0x10001), sr, 1024, 80));
        AsymmetricCipherKeyPair keypair = gen.generateKeyPair();
        System.out.println("Generated keypair, extracting components and creating public structure for certificate");
        RSAKeyParameters publicKey = (RSAKeyParameters) keypair.getPublic();
        privateKey = (RSAPrivateCrtKeyParameters) keypair.getPrivate();
        RSAPublicKeyStructure pkStruct = new RSAPublicKeyStructure(publicKey.getModulus(), publicKey.getExponent());
        System.out.println("New public key is '" + new String(Hex.encode(pkStruct.getEncoded())) + ", exponent=" + publicKey.getExponent() + ", modulus=" + publicKey.getModulus());
        pubKey = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(publicKey.getModulus(), publicKey.getExponent()));
        privKey = KeyFactory.getInstance("RSA").generatePrivate(new RSAPrivateCrtKeySpec(publicKey.getModulus(), publicKey.getExponent(), privateKey.getExponent(), privateKey.getP(), privateKey.getQ(), privateKey.getDP(), privateKey.getDQ(), privateKey.getQInv()));
        Calendar expiry = Calendar.getInstance();
        expiry.add(Calendar.DAY_OF_YEAR, validityDays);
        X500Name x509Name = new X500Name("CN=" + commonName);
        V3TBSCertificateGenerator certGen = new V3TBSCertificateGenerator();
        certGen.setSerialNumber(new DERInteger(BigInteger.valueOf(System.currentTimeMillis())));
        if (caCert != null) {
            certGen.setIssuer(PrincipalUtil.getSubjectX509Principal(caCert));
        } else {
            certGen.setIssuer(x509Name);
        }
        certGen.setSubject(x509Name);
        DERObjectIdentifier sigOID = PKCSObjectIdentifiers.sha1WithRSAEncryption;
        AlgorithmIdentifier sigAlgId = new AlgorithmIdentifier(sigOID, new DERNull());
        certGen.setSignature(sigAlgId);
        certGen.setSubjectPublicKeyInfo(new SubjectPublicKeyInfo((ASN1Sequence) new ASN1InputStream(new ByteArrayInputStream(pubKey.getEncoded())).readObject()));
        certGen.setStartDate(new Time(new Date(System.currentTimeMillis())));
        certGen.setEndDate(new Time(expiry.getTime()));
        Hashtable extensions = new Hashtable();
        Vector extOrdering = new Vector();
        addExtensionHelper(X509Extension.subjectKeyIdentifier, false, new SubjectKeyIdentifierStructure(pubKey), extOrdering, extensions);
        if (caCert != null) {
            addExtensionHelper(X509Extension.authorityKeyIdentifier, false, new AuthorityKeyIdentifierStructure(caCert), extOrdering, extensions);
        } else {
            addExtensionHelper(X509Extension.basicConstraints, true, new BasicConstraints(0), extOrdering, extensions);
        }
        certGen.setExtensions(new X509Extensions(extOrdering, extensions));
        System.out.println("Certificate structure generated, creating SHA1 digest");
        SHA1Digest digester = new SHA1Digest();
        AsymmetricBlockCipher rsa = new PKCS1Encoding(new RSAEngine());
        TBSCertificateStructure tbsCert = certGen.generateTBSCertificate();
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        DEROutputStream dOut = new DEROutputStream(bOut);
        dOut.writeObject(tbsCert);
        byte[] signature;
        byte[] certBlock = bOut.toByteArray();
        System.out.println("Block to sign is '" + new String(Hex.encode(certBlock)) + "'");
        digester.update(certBlock, 0, certBlock.length);
        byte[] hash = new byte[digester.getDigestSize()];
        digester.doFinal(hash, 0);
        if (caCert != null) {
            rsa.init(true, caPrivateKey);
        } else {
            System.out.println("No CA has been set, creating self-signed certificate as a new CA");
            rsa.init(true, privateKey);
        }
        DigestInfo dInfo = new DigestInfo(new AlgorithmIdentifier(X509ObjectIdentifiers.id_SHA1, null), hash);
        byte[] digest = dInfo.getEncoded(ASN1Encodable.DER);
        signature = rsa.processBlock(digest, 0, digest.length);
        System.out.println("SHA1/RSA signature of digest is '" + new String(Hex.encode(signature)) + "'");
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(tbsCert);
        v.add(sigAlgId);
        v.add(new DERBitString(signature));
        X509CertificateObject clientCert = new X509CertificateObject(new X509CertificateStructure(new DERSequence(v)));
        System.out.println("Verifying certificate for correct signature with CA public key");
        System.out.println("Exporting certificate in PKCS12 format");
        PKCS12BagAttributeCarrier bagCert = clientCert;
        bagCert.setBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_friendlyName, new DERBMPString(exportAlias == null ? CertificateExportFriendlyName : exportAlias));
        bagCert.setBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_localKeyId, new SubjectKeyIdentifierStructure(pubKey));
        JDKPKCS12KeyStore store;
        store = new JDKPKCS12KeyStore.BCPKCS12KeyStore();
        store.engineLoad(null, null);
        FileOutputStream fOut = new FileOutputStream(exportFile);
        X509Certificate[] chain;
        if (caCert != null) {
            chain = new X509Certificate[2];
            chain[0] = clientCert;
            chain[1] = caCert;
        } else {
            chain = new X509Certificate[1];
            chain[0] = clientCert;
        }
        store.engineSetKeyEntry(exportAlias == null ? KeyExportFriendlyName : exportAlias, privKey, exportPassword.toCharArray(), chain);
        store.engineStore(fOut, exportPassword.toCharArray());
        return true;
    }
