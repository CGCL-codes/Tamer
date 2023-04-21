    public void storeDataBlock(byte[] allData, int offs, int len, String password, RandomAccessFile raf) throws IOException {
        byte[] iv, data, salt = new byte[16];
        rnd.nextBytes(salt);
        try {
            Cipher cip = getCipher(password, Cipher.ENCRYPT_MODE, salt, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStream out = new GZIPOutputStream(baos);
            out.write(allData, offs, len);
            out.close();
            data = baos.toByteArray();
            baos.reset();
            out = new CipherOutputStream(baos, cip);
            if (data.length >= RAW_SIZE - IV_LENGTH - 17) throw new IOException("Data too large");
            out.write(makePadding(RAW_SIZE - data.length - IV_LENGTH - 17));
            out.write(42);
            out.write(data);
            out.close();
            data = baos.toByteArray();
            iv = cip.getIV();
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
            throw new IOException(ex.toString());
        }
        raf.write(salt);
        raf.write(iv);
        raf.write(data);
        if (salt.length + iv.length + data.length != RAW_SIZE) {
            throw new RuntimeException("Assertion failed!");
        }
    }
