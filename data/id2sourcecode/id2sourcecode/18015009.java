    public void create(char[] password) throws IOException {
        FileChannel c = null;
        try {
            Key key = generatePBEKey(password);
            Cipher pbeCipher = getPBECipher(key);
            AlgorithmParameters params = pbeCipher.getParameters();
            byte[] paramBuf = params.getEncoded();
            symKey = generateSymmetricKey();
            byte[] encKey = pbeCipher.doFinal(symKey.getEncoded());
            byte[] verification = encrypt(VERIFICATION_STRING.clone());
            ByteBuffer buf = ByteBuffer.allocate(8192);
            c = new FileOutputStream(file).getChannel();
            SecureRandom r = new SecureRandom();
            r.nextBytes(buf.array());
            buf.limit(buf.capacity());
            c.write(buf);
            c.position(0);
            buf.clear();
            buf.put((byte) paramBuf.length);
            buf.put(paramBuf);
            buf.put((byte) encKey.length);
            buf.put(encKey);
            buf.flip();
            c.write(buf);
            buf.clear();
            c.position(VERIFICATION_OFFSET);
            buf.put((byte) verification.length);
            buf.put(verification);
            buf.putInt(DATABASE_VERSION);
            buf.flip();
            c.write(buf);
            version = DATABASE_VERSION;
            buf.clear();
            c.position(SERIAL_OFFSET);
            buf.putLong(serialNumber);
            buf.flip();
            c.write(buf);
            buf.clear();
            c.position(IV_OFFSET);
            buf.put((byte) 0);
            buf.flip();
            c.write(buf);
            buf.clear();
            c.position(DATALEN_OFFSET);
            buf.putInt(0);
            buf.flip();
            c.write(buf);
            buf.clear();
            c.position(DATA_OFFSET);
            buf.putInt(0);
            buf.putInt(0);
            buf.putInt(0);
            buf.flip();
            c.write(buf);
        } catch (GeneralSecurityException e) {
            log.log(Level.SEVERE, "Unable to initialize crypto", e);
            IOException ioe = new IOException("Unable to initialize crypto");
            ioe.initCause(e);
            throw ioe;
        } finally {
            if (c != null) c.close();
        }
    }
