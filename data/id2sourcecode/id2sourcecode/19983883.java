        Object getObject(char[] password) throws NoSuchAlgorithmException, UnrecoverableKeyException {
            if (password == null || password.length == 0) {
                if (obj instanceof Key) {
                    return obj;
                }
            }
            if (type == SEALED) {
                ByteArrayInputStream bIn = new ByteArrayInputStream((byte[]) obj);
                DataInputStream dIn = new DataInputStream(bIn);
                try {
                    byte[] salt = new byte[dIn.readInt()];
                    dIn.readFully(salt);
                    int iterationCount = dIn.readInt();
                    Cipher cipher = makePBECipher(KEY_CIPHER, Cipher.DECRYPT_MODE, password, salt, iterationCount);
                    CipherInputStream cIn = new CipherInputStream(dIn, cipher);
                    try {
                        return decodeKey(new DataInputStream(cIn));
                    } catch (Exception x) {
                        bIn = new ByteArrayInputStream((byte[]) obj);
                        dIn = new DataInputStream(bIn);
                        salt = new byte[dIn.readInt()];
                        dIn.readFully(salt);
                        iterationCount = dIn.readInt();
                        cipher = makePBECipher("Broken" + KEY_CIPHER, Cipher.DECRYPT_MODE, password, salt, iterationCount);
                        cIn = new CipherInputStream(dIn, cipher);
                        Key k = null;
                        try {
                            k = decodeKey(new DataInputStream(cIn));
                        } catch (Exception y) {
                            bIn = new ByteArrayInputStream((byte[]) obj);
                            dIn = new DataInputStream(bIn);
                            salt = new byte[dIn.readInt()];
                            dIn.readFully(salt);
                            iterationCount = dIn.readInt();
                            cipher = makePBECipher("Old" + KEY_CIPHER, Cipher.DECRYPT_MODE, password, salt, iterationCount);
                            cIn = new CipherInputStream(dIn, cipher);
                            k = decodeKey(new DataInputStream(cIn));
                        }
                        if (k != null) {
                            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                            DataOutputStream dOut = new DataOutputStream(bOut);
                            dOut.writeInt(salt.length);
                            dOut.write(salt);
                            dOut.writeInt(iterationCount);
                            Cipher out = makePBECipher(KEY_CIPHER, Cipher.ENCRYPT_MODE, password, salt, iterationCount);
                            CipherOutputStream cOut = new CipherOutputStream(dOut, out);
                            dOut = new DataOutputStream(cOut);
                            encodeKey(k, dOut);
                            dOut.close();
                            obj = bOut.toByteArray();
                            return k;
                        } else {
                            throw new UnrecoverableKeyException("no match");
                        }
                    }
                } catch (Exception e) {
                    throw new UnrecoverableKeyException("no match");
                }
            } else {
                throw new RuntimeException("forget something!");
            }
        }
