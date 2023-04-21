        private void generatePrivacyKeyPair(boolean clientMode) throws IOException, UnsupportedEncodingException, NoSuchAlgorithmException, SaslException {
            byte[] ccmagic = CLIENT_CONF_MAGIC.getBytes(encoding);
            byte[] scmagic = SVR_CONF_MAGIC.getBytes(encoding);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            int n;
            if (negotiatedCipher.equals(CIPHER_TOKENS[RC4_40])) {
                n = 5;
            } else if (negotiatedCipher.equals(CIPHER_TOKENS[RC4_56])) {
                n = 7;
            } else {
                n = 16;
            }
            byte[] keyBuffer = new byte[n + ccmagic.length];
            System.arraycopy(H_A1, 0, keyBuffer, 0, n);
            System.arraycopy(ccmagic, 0, keyBuffer, n, ccmagic.length);
            md5.update(keyBuffer);
            byte[] Kcc = md5.digest();
            System.arraycopy(scmagic, 0, keyBuffer, n, scmagic.length);
            md5.update(keyBuffer);
            byte[] Kcs = md5.digest();
            if (logger.isLoggable(Level.FINER)) {
                traceOutput(DP_CLASS_NAME, "generatePrivacyKeyPair", "DIGEST24:Kcc: ", Kcc);
                traceOutput(DP_CLASS_NAME, "generatePrivacyKeyPair", "DIGEST25:Kcs: ", Kcs);
            }
            byte[] myKc;
            byte[] peerKc;
            if (clientMode) {
                myKc = Kcc;
                peerKc = Kcs;
            } else {
                myKc = Kcs;
                peerKc = Kcc;
            }
            try {
                SecretKey encKey;
                SecretKey decKey;
                if (negotiatedCipher.indexOf(CIPHER_TOKENS[RC4]) > -1) {
                    encCipher = Cipher.getInstance("RC4");
                    decCipher = Cipher.getInstance("RC4");
                    encKey = new SecretKeySpec(myKc, "RC4");
                    decKey = new SecretKeySpec(peerKc, "RC4");
                    encCipher.init(Cipher.ENCRYPT_MODE, encKey);
                    decCipher.init(Cipher.DECRYPT_MODE, decKey);
                } else if ((negotiatedCipher.equals(CIPHER_TOKENS[DES])) || (negotiatedCipher.equals(CIPHER_TOKENS[DES3]))) {
                    String cipherFullname, cipherShortname;
                    if (negotiatedCipher.equals(CIPHER_TOKENS[DES])) {
                        cipherFullname = "DES/CBC/NoPadding";
                        cipherShortname = "des";
                    } else {
                        cipherFullname = "DESede/CBC/NoPadding";
                        cipherShortname = "desede";
                    }
                    encCipher = Cipher.getInstance(cipherFullname);
                    decCipher = Cipher.getInstance(cipherFullname);
                    encKey = makeDesKeys(myKc, cipherShortname);
                    decKey = makeDesKeys(peerKc, cipherShortname);
                    IvParameterSpec encIv = new IvParameterSpec(myKc, 8, 8);
                    IvParameterSpec decIv = new IvParameterSpec(peerKc, 8, 8);
                    encCipher.init(Cipher.ENCRYPT_MODE, encKey, encIv);
                    decCipher.init(Cipher.DECRYPT_MODE, decKey, decIv);
                    if (logger.isLoggable(Level.FINER)) {
                        traceOutput(DP_CLASS_NAME, "generatePrivacyKeyPair", "DIGEST26:" + negotiatedCipher + " IVcc: ", encIv.getIV());
                        traceOutput(DP_CLASS_NAME, "generatePrivacyKeyPair", "DIGEST27:" + negotiatedCipher + " IVcs: ", decIv.getIV());
                        traceOutput(DP_CLASS_NAME, "generatePrivacyKeyPair", "DIGEST28:" + negotiatedCipher + " encryption key: ", encKey.getEncoded());
                        traceOutput(DP_CLASS_NAME, "generatePrivacyKeyPair", "DIGEST29:" + negotiatedCipher + " decryption key: ", decKey.getEncoded());
                    }
                }
            } catch (InvalidKeySpecException e) {
                throw new SaslException("DIGEST-MD5: Unsupported key " + "specification used.", e);
            } catch (InvalidAlgorithmParameterException e) {
                throw new SaslException("DIGEST-MD5: Invalid cipher " + "algorithem parameter used to create cipher instance", e);
            } catch (NoSuchPaddingException e) {
                throw new SaslException("DIGEST-MD5: Unsupported " + "padding used for chosen cipher", e);
            } catch (InvalidKeyException e) {
                throw new SaslException("DIGEST-MD5: Invalid data " + "used to initialize keys", e);
            }
        }
