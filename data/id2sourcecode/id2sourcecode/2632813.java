    public void encrypt(int version, InputStream in, OutputStream out) throws IOException, GeneralSecurityException {
        try {
            byte[] text = null;
            ivSpec1 = new IvParameterSpec(generateIv1());
            aesKey1 = new SecretKeySpec(generateAESKey1(ivSpec1.getIV(), password), CRYPT_ALG);
            ivSpec2 = new IvParameterSpec(generateIV2());
            aesKey2 = new SecretKeySpec(generateAESKey2(), CRYPT_ALG);
            debug("IV1: ", ivSpec1.getIV());
            debug("AES1: ", aesKey1.getEncoded());
            debug("IV2: ", ivSpec2.getIV());
            debug("AES2: ", aesKey2.getEncoded());
            out.write("AES".getBytes("UTF-8"));
            out.write(version);
            out.write(0);
            if (version == 2) {
                out.write(0);
                out.write(0);
            }
            out.write(ivSpec1.getIV());
            text = new byte[BLOCK_SIZE + KEY_SIZE];
            cipher.init(Cipher.ENCRYPT_MODE, aesKey1, ivSpec1);
            cipher.update(ivSpec2.getIV(), 0, BLOCK_SIZE, text);
            cipher.doFinal(aesKey2.getEncoded(), 0, KEY_SIZE, text, BLOCK_SIZE);
            out.write(text);
            debug("IV2 + AES2 ciphertext: ", text);
            hmac.init(new SecretKeySpec(aesKey1.getEncoded(), HMAC_ALG));
            text = hmac.doFinal(text);
            out.write(text);
            debug("HMAC1: ", text);
            cipher.init(Cipher.ENCRYPT_MODE, aesKey2, ivSpec2);
            hmac.init(new SecretKeySpec(aesKey2.getEncoded(), HMAC_ALG));
            text = new byte[BLOCK_SIZE];
            int len, last = 0;
            while ((len = in.read(text)) > 0) {
                cipher.update(text, 0, BLOCK_SIZE, text);
                hmac.update(text);
                out.write(text);
                last = len;
            }
            last &= 0x0f;
            out.write(last);
            debug("Last block size mod 16: " + last);
            text = hmac.doFinal();
            out.write(text);
            debug("HMAC2: ", text);
        } catch (InvalidKeyException e) {
            throw new GeneralSecurityException(JCE_EXCEPTION_MESSAGE, e);
        }
    }
