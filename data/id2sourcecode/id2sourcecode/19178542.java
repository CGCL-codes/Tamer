    protected void setupCrypto() throws IOException {
        try {
            SHA1Hasher hasher = new SHA1Hasher();
            hasher.update(KEYA_IV);
            hasher.update(secret_bytes);
            hasher.update(shared_secret);
            byte[] a_key = hasher.getDigest();
            hasher = new SHA1Hasher();
            hasher.update(KEYB_IV);
            hasher.update(secret_bytes);
            hasher.update(shared_secret);
            byte[] b_key = hasher.getDigest();
            SecretKeySpec secret_key_spec_a = new SecretKeySpec(a_key, RC4_STREAM_ALG);
            SecretKeySpec secret_key_spec_b = new SecretKeySpec(b_key, RC4_STREAM_ALG);
            write_cipher = new TransportCipher(RC4_STREAM_CIPHER, Cipher.ENCRYPT_MODE, outbound ? secret_key_spec_a : secret_key_spec_b);
            read_cipher = new TransportCipher(RC4_STREAM_CIPHER, Cipher.DECRYPT_MODE, outbound ? secret_key_spec_b : secret_key_spec_a);
        } catch (Throwable e) {
            e.printStackTrace();
            throw (new IOException(Debug.getNestedExceptionMessage(e)));
        }
    }
