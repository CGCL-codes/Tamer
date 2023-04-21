    @Test
    public final void testPortableSerialization() {
        System.err.println("CipherTextTest.testPortableSerialization()...");
        String filename = "ciphertext-portable.ser";
        File serializedFile = new File(filename);
        serializedFile.delete();
        int keySize = 128;
        if (CryptoPolicy.isUnlimitedStrengthCryptoAvailable()) {
            keySize = 256;
        }
        CipherSpec cipherSpec = new CipherSpec(encryptor, keySize);
        cipherSpec.setIV(ivSpec.getIV());
        SecretKey key;
        try {
            key = CryptoHelper.generateSecretKey(cipherSpec.getCipherAlgorithm(), keySize);
            encryptor.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            byte[] raw = encryptor.doFinal("This is my secret message!!!".getBytes("UTF8"));
            CipherText ciphertext = new CipherText(cipherSpec, raw);
            SecretKey authKey = CryptoHelper.computeDerivedKey(key, key.getEncoded().length * 8, "authenticity");
            ciphertext.computeAndStoreMAC(authKey);
            byte[] serializedBytes = ciphertext.asPortableSerializedByteArray();
            FileOutputStream fos = new FileOutputStream(serializedFile);
            fos.write(serializedBytes);
            fos.close();
            FileInputStream fis = new FileInputStream(serializedFile);
            int avail = fis.available();
            byte[] bytes = new byte[avail];
            fis.read(bytes, 0, avail);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ;
            }
            CipherText restoredCipherText = CipherText.fromPortableSerializedBytes(bytes);
            assertTrue(ciphertext.equals(restoredCipherText));
        } catch (EncryptionException e) {
            Assert.fail("Caught EncryptionException: " + e);
        } catch (FileNotFoundException e) {
            Assert.fail("Caught FileNotFoundException: " + e);
        } catch (IOException e) {
            Assert.fail("Caught IOException: " + e);
        } catch (Exception e) {
            Assert.fail("Caught Exception: " + e);
        } finally {
            serializedFile.delete();
        }
    }
