    @Test
    public void simple() throws IOException {
        byte[] key = new byte[32];
        Random rnd = new Random();
        rnd.nextBytes(key);
        ByteArrayOutputStream encrypted = new ByteArrayOutputStream();
        CryptOutputStream output = new CryptOutputStream(encrypted, key);
        byte[] plain = new byte[DATA_SIZE];
        rnd.nextBytes(plain);
        output.write(plain);
        output.close();
        CryptInputStream decrypter = new CryptInputStream(new ByteArrayInputStream(encrypted.toByteArray()), key);
        ByteArrayOutputStream decrypted = new ByteArrayOutputStream();
        for (; ; ) {
            int readed = decrypter.read();
            if (readed < 0) {
                break;
            }
            decrypted.write(readed);
        }
        decrypted.close();
        Assert.assertEquals(plain.length, decrypted.toByteArray().length);
        Assert.assertTrue(Arrays.equals(plain, decrypted.toByteArray()));
    }
