    public void decrypt(long inSize, InputStream in, OutputStream out) throws IOException, GeneralSecurityException {
        try {
            byte[] text = null, backup = null;
            long total = 3 + 1 + 1 + BLOCK_SIZE + BLOCK_SIZE + KEY_SIZE + SHA_SIZE + 1 + SHA_SIZE;
            int version;
            text = new byte[3];
            readBytes(in, text);
            if (!new String(text, "UTF-8").equals("AES")) {
                throw new IOException("Invalid file header");
            }
            version = in.read();
            if (version < 1 || version > 2) {
                throw new IOException("Unsupported version number: " + version);
            }
            debug("Version: " + version);
            in.read();
            if (version == 2) {
                text = new byte[2];
                int len;
                do {
                    readBytes(in, text);
                    len = ((0xff & (int) text[0]) << 8) | (0xff & (int) text[1]);
                    if (in.skip(len) != len) {
                        throw new IOException("Unexpected end of extension");
                    }
                    total += 2 + len;
                    debug("Skipped extension sized: " + len);
                } while (len != 0);
            }
            text = new byte[BLOCK_SIZE];
            readBytes(in, text);
            ivSpec1 = new IvParameterSpec(text);
            aesKey1 = new SecretKeySpec(generateAESKey1(ivSpec1.getIV(), password), CRYPT_ALG);
            debug("IV1: ", ivSpec1.getIV());
            debug("AES1: ", aesKey1.getEncoded());
            cipher.init(Cipher.DECRYPT_MODE, aesKey1, ivSpec1);
            backup = new byte[BLOCK_SIZE + KEY_SIZE];
            readBytes(in, backup);
            debug("IV2 + AES2 ciphertext: ", backup);
            text = cipher.doFinal(backup);
            ivSpec2 = new IvParameterSpec(text, 0, BLOCK_SIZE);
            aesKey2 = new SecretKeySpec(text, BLOCK_SIZE, KEY_SIZE, CRYPT_ALG);
            debug("IV2: ", ivSpec2.getIV());
            debug("AES2: ", aesKey2.getEncoded());
            hmac.init(new SecretKeySpec(aesKey1.getEncoded(), HMAC_ALG));
            backup = hmac.doFinal(backup);
            text = new byte[SHA_SIZE];
            readBytes(in, text);
            if (!Arrays.equals(backup, text)) {
                throw new IOException("Message has been altered or password incorrect");
            }
            debug("HMAC1: ", text);
            total = inSize - total;
            if (total % BLOCK_SIZE != 0) {
                throw new IOException("Input file is corrupt");
            }
            if (total == 0) {
                in.read();
            }
            debug("Payload size: " + total);
            cipher.init(Cipher.DECRYPT_MODE, aesKey2, ivSpec2);
            hmac.init(new SecretKeySpec(aesKey2.getEncoded(), HMAC_ALG));
            backup = new byte[BLOCK_SIZE];
            text = new byte[BLOCK_SIZE];
            for (int block = (int) (total / BLOCK_SIZE); block > 0; block--) {
                int len = BLOCK_SIZE;
                if (in.read(backup, 0, len) != len) {
                    throw new IOException("Unexpected end of file contents");
                }
                cipher.update(backup, 0, len, text);
                hmac.update(backup, 0, len);
                if (block == 1) {
                    int last = in.read();
                    debug("Last block size mod 16: " + last);
                    len = (last > 0 ? last : BLOCK_SIZE);
                }
                out.write(text, 0, len);
            }
            out.write(cipher.doFinal());
            backup = hmac.doFinal();
            text = new byte[SHA_SIZE];
            readBytes(in, text);
            if (!Arrays.equals(backup, text)) {
                throw new IOException("Message has been altered or password incorrect");
            }
            debug("HMAC2: ", text);
        } catch (InvalidKeyException e) {
            throw new GeneralSecurityException(JCE_EXCEPTION_MESSAGE, e);
        }
    }
