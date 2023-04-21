    @Test
    public final void testJavaSerialization() {
        String filename = "ciphertext.ser";
        File serializedFile = new File(filename);
        try {
            serializedFile.delete();
            CipherSpec cipherSpec = new CipherSpec(encryptor, 128);
            cipherSpec.setIV(ivSpec.getIV());
            SecretKey key = CryptoHelper.generateSecretKey(cipherSpec.getCipherAlgorithm(), 128);
            encryptor.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            byte[] raw = encryptor.doFinal("This is my secret message!!!".getBytes("UTF8"));
            CipherText ciphertext = new CipherText(cipherSpec, raw);
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(ciphertext);
            out.close();
            fos.close();
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fis);
            CipherText restoredCipherText = (CipherText) in.readObject();
            in.close();
            fis.close();
            assertEquals("1: Serialized restored CipherText differs from saved CipherText", ciphertext.toString(), restoredCipherText.toString());
            assertArrayEquals("2: Serialized restored CipherText differs from saved CipherText", ciphertext.getIV(), restoredCipherText.getIV());
            assertEquals("3: Serialized restored CipherText differs from saved CipherText", ciphertext.getBase64EncodedRawCipherText(), restoredCipherText.getBase64EncodedRawCipherText());
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            fail("testJavaSerialization(): Unexpected IOException: " + ex);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace(System.err);
            fail("testJavaSerialization(): Unexpected ClassNotFoundException: " + ex);
        } catch (EncryptionException ex) {
            ex.printStackTrace(System.err);
            fail("testJavaSerialization(): Unexpected EncryptionException: " + ex);
        } catch (IllegalBlockSizeException ex) {
            ex.printStackTrace(System.err);
            fail("testJavaSerialization(): Unexpected IllegalBlockSizeException: " + ex);
        } catch (BadPaddingException ex) {
            ex.printStackTrace(System.err);
            fail("testJavaSerialization(): Unexpected BadPaddingException: " + ex);
        } catch (InvalidKeyException ex) {
            ex.printStackTrace(System.err);
            fail("testJavaSerialization(): Unexpected InvalidKeyException: " + ex);
        } catch (InvalidAlgorithmParameterException ex) {
            ex.printStackTrace(System.err);
            fail("testJavaSerialization(): Unexpected InvalidAlgorithmParameterException: " + ex);
        } finally {
            serializedFile.delete();
        }
    }
