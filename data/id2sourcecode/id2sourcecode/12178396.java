    public void testWriteBlock() throws Exception {
        Key key = new SecretKeySpec("1234567890ABCDEF".getBytes(), "AES");
        Cipher encrypt = Cipher.getInstance("AES");
        encrypt.init(Cipher.ENCRYPT_MODE, key);
        Cipher decrypt = Cipher.getInstance("AES");
        decrypt.init(Cipher.DECRYPT_MODE, key);
        m_cipherPacketWriter = new CipherPacketWriter(encrypt, new RegularPacketWriter(1, true));
        ByteBuffer[] result = m_cipherPacketWriter.write(new ByteBuffer[] { ByteBuffer.wrap("ABC".getBytes()) });
        assertEquals("[3, 65, 66, 67]", Arrays.toString(decrypt.doFinal(merge(result))));
        result = m_cipherPacketWriter.write(new ByteBuffer[] { ByteBuffer.wrap("C".getBytes()), ByteBuffer.wrap("D".getBytes()) });
        assertEquals("[2, 67, 68]", Arrays.toString(decrypt.doFinal(merge(result))));
    }
