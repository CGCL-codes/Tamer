    public void test_doFinal() throws Exception {
        for (int index = 1; index < 4; index++) {
            Cipher c = Cipher.getInstance("DESEDE/CBC/PKCS5Padding");
            byte[] keyMaterial = loadBytes("hyts_" + "des-ede3-cbc.test" + index + ".key");
            DESedeKeySpec keySpec = new DESedeKeySpec(keyMaterial);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DESEDE");
            Key k = skf.generateSecret(keySpec);
            byte[] ivMaterial = loadBytes("hyts_" + "des-ede3-cbc.test" + index + ".iv");
            IvParameterSpec iv = new IvParameterSpec(ivMaterial);
            c.init(Cipher.ENCRYPT_MODE, k, iv);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] input = new byte[256];
            String resPath = "hyts_" + "des-ede3-cbc.test" + index + ".plaintext";
            InputStream is = Support_Resources.getResourceStream(resPath);
            int bytesRead = is.read(input, 0, 256);
            while (bytesRead > 0) {
                byte[] output = c.update(input, 0, bytesRead);
                if (output != null) {
                    baos.write(output);
                }
                bytesRead = is.read(input, 0, 256);
            }
            byte[] output = c.doFinal();
            if (output != null) {
                baos.write(output);
            }
            byte[] encryptedPlaintext = baos.toByteArray();
            is.close();
            byte[] cipherText = loadBytes("hyts_" + "des-ede3-cbc.test" + index + ".ciphertext");
            assertTrue("Operation produced incorrect results", Arrays.equals(encryptedPlaintext, cipherText));
        }
    }
