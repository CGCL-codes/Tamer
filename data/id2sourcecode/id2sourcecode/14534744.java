    public void testDigest() {
        byte[] data = "test".getBytes();
        try {
            String b64 = CryptoUtils.digest(data, "SHA");
            assertEquals("qUqP5cyxm6YcTAhz05Hph5gvu9M=", b64);
        } catch (CryptoException e) {
            assertEquals("", e.getMessage());
        }
        data = new byte[1024];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (i & 0xFF);
        }
        try {
            String b64 = CryptoUtils.digest(data, "SHA");
            assertEquals("WwBmnEgNXP+9+ovbqZVhFg8tG3c=", b64);
        } catch (CryptoException e) {
            assertEquals("", e.getMessage());
        }
    }
