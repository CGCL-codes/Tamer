    public void testServices() throws Exception {
        IEncryptionProvider provider = new SimpleEncryptionProvider(SimpleEncryptionKey.createUsing(Strength.STRONG, "AES", "Hello World".getBytes()));
        String original = "This is some text";
        byte[] encrypted = provider.encrypt(original.getBytes());
        assert new String(provider.decrypt(encrypted)).equals(original);
        ByteArrayOutputStream encryptedStream = new ByteArrayOutputStream();
        OutputStream output = provider.getEncryptingOutputStream(encryptedStream);
        StringBuffer originalBuffer = new StringBuffer();
        for (int i = 0; i < 100; i++) {
            String data = StringUtil.repeat("Hello World", i);
            output.write(data.getBytes());
            originalBuffer.append(data);
        }
        output.close();
        String decryptedFirst = new String(provider.decrypt(encryptedStream.toByteArray()));
        assert decryptedFirst.equals(originalBuffer.toString());
        InputStream input = provider.getDecryptingInputStream(new ByteArrayInputStream(encryptedStream.toByteArray()));
        int read = -1;
        byte[] buffer = new byte[200];
        ByteArrayOutputStream decrypted = new ByteArrayOutputStream();
        while ((read = input.read(buffer)) != -1) {
            decrypted.write(ByteUtil.getSlice(buffer, 0, read));
        }
        assert decryptedFirst.equals(new String(decrypted.toByteArray()));
    }
