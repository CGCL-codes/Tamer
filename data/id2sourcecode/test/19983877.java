        StoreEntry(String alias, Key key, char[] password, Certificate[] certChain) throws Exception {
            this.type = SEALED;
            this.alias = alias;
            this.certChain = certChain;
            byte[] salt = new byte[KEY_SALT_SIZE];
            random.setSeed(System.currentTimeMillis());
            random.nextBytes(salt);
            int iterationCount = MIN_ITERATIONS + (random.nextInt() & 0x3ff);
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            DataOutputStream dOut = new DataOutputStream(bOut);
            dOut.writeInt(salt.length);
            dOut.write(salt);
            dOut.writeInt(iterationCount);
            Cipher cipher = makePBECipher(KEY_CIPHER, Cipher.ENCRYPT_MODE, password, salt, iterationCount);
            CipherOutputStream cOut = new CipherOutputStream(dOut, cipher);
            dOut = new DataOutputStream(cOut);
            encodeKey(key, dOut);
            dOut.close();
            obj = bOut.toByteArray();
        }
