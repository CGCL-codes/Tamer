        private EncryptedWrapper() throws SaslException {
            super();
            try {
                byte[] serverSalt = "Digest H(A1) to server-to-client sealing key magic constant".getBytes("ISO-8859-1");
                byte[] clientSalt = "Digest H(A1) to client-to-server sealing key magic constant".getBytes("ISO-8859-1");
                int n;
                if (sessionCipher.equals(CIPHERSUITS[RC4_40])) {
                    n = 5;
                } else if (sessionCipher.equals(CIPHERSUITS[RC4_56])) {
                    n = 7;
                } else {
                    n = 16;
                }
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                byte[] temp = new byte[n + serverSalt.length];
                System.arraycopy(MD5DigestSessionKey, 0, temp, 0, n);
                System.arraycopy(serverSalt, 0, temp, n, serverSalt.length);
                byte[] kcs = messageDigest.digest(temp);
                System.arraycopy(clientSalt, 0, temp, n, clientSalt.length);
                byte[] kcc = messageDigest.digest(temp);
                SecretKey encodingKey;
                SecretKey decodingKey;
                if ((sessionCipher.equals(CIPHERSUITS[DES])) || (sessionCipher.equals(CIPHERSUITS[DES3]))) {
                    String cipherName;
                    if (sessionCipher.equals(CIPHERSUITS[DES])) {
                        cipherName = "DES/CBC/NoPadding";
                        encodingKey = createDesKey(kcs);
                        decodingKey = createDesKey(kcc);
                    } else {
                        cipherName = "DESede/CBC/NoPadding";
                        encodingKey = createDesedeKey(kcs);
                        decodingKey = createDesedeKey(kcc);
                    }
                    encodingCipher = Cipher.getInstance(cipherName);
                    IvParameterSpec encodingIV = new IvParameterSpec(kcs, 8, 8);
                    encodingCipher.init(Cipher.ENCRYPT_MODE, encodingKey, encodingIV);
                    decodingCipher = Cipher.getInstance(cipherName);
                    IvParameterSpec decodingIV = new IvParameterSpec(kcc, 8, 8);
                    decodingCipher.init(Cipher.DECRYPT_MODE, decodingKey, decodingIV);
                } else {
                    encodingCipher = Cipher.getInstance("RC4");
                    encodingKey = new SecretKeySpec(kcs, "RC4");
                    encodingCipher.init(Cipher.ENCRYPT_MODE, encodingKey);
                    decodingCipher = Cipher.getInstance("RC4");
                    decodingKey = new SecretKeySpec(kcc, "RC4");
                    decodingCipher.init(Cipher.DECRYPT_MODE, decodingKey);
                }
            } catch (UnsupportedEncodingException e) {
                throw new SaslException(e.getMessage());
            } catch (GeneralSecurityException e) {
                throw new SaslException(e.getMessage());
            }
        }
