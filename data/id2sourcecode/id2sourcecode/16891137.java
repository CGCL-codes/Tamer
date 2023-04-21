    public static byte[] writeKeyPair(ASCIIArmour armour, String password, SecureRandom random, KeyPair keyPair) throws SSH2FatalException {
        ASN1Object pem;
        PublicKey publicKey = keyPair.getPublic();
        int headType;
        if (publicKey instanceof DSAPublicKey) {
            DSAPublicKey pubKey = (DSAPublicKey) keyPair.getPublic();
            DSAPrivateKey prvKey = (DSAPrivateKey) keyPair.getPrivate();
            DSAParams params = pubKey.getParams();
            PEMDSAPrivate dsa = new PEMDSAPrivate(0, params.getP(), params.getQ(), params.getG(), pubKey.getY(), prvKey.getX());
            pem = dsa;
            headType = TYPE_PEM_DSA;
        } else if (publicKey instanceof RSAPublicKey) {
            RSAPublicKey pubKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateCrtKey prvKey = (RSAPrivateCrtKey) keyPair.getPrivate();
            RSAPrivateKey rsa = new RSAPrivateKey(0, pubKey.getModulus(), pubKey.getPublicExponent(), prvKey.getPrivateExponent(), prvKey.getPrimeP(), prvKey.getPrimeQ(), prvKey.getCrtCoefficient());
            pem = rsa;
            headType = TYPE_PEM_RSA;
        } else {
            throw new SSH2FatalException("Unsupported key type: " + publicKey);
        }
        armour.setHeaderLine(BEGIN_PRV_KEY[headType]);
        armour.setTailLine(END_PRV_KEY[headType]);
        ByteArrayOutputStream enc = new ByteArrayOutputStream(128);
        ASN1DER der = new ASN1DER();
        try {
            der.encode(enc, pem);
        } catch (IOException e) {
            throw new SSH2FatalException("Error while DER encoding");
        }
        byte[] keyBlob = enc.toByteArray();
        if (password != null && password.length() > 0) {
            byte[] iv = new byte[8];
            byte[] key;
            random.setSeed(keyBlob);
            for (int i = 0; i < 8; i++) {
                byte[] r = new byte[1];
                do {
                    random.nextBytes(r);
                    iv[i] = r[0];
                } while (iv[i] == 0x00);
            }
            key = expandPasswordToKey(password, 192 / 8, iv);
            armour.setHeaderField(PRV_PROCTYPE, "4,ENCRYPTED");
            armour.setHeaderField(PRV_DEKINFO, "DES-EDE3-CBC," + HexDump.toString(iv).toUpperCase());
            int encLen = (8 - (keyBlob.length % 8)) + keyBlob.length;
            byte[] encBuf = new byte[encLen];
            doCipher(Cipher.ENCRYPT_MODE, password, keyBlob, keyBlob.length, encBuf, iv);
            keyBlob = encBuf;
        }
        return keyBlob;
    }
