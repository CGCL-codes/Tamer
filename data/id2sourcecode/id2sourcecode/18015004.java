    public void changePassword(char[] password) throws IOException {
        FileChannel c = null;
        if (!file.exists()) throw new FileNotFoundException(path);
        RandomAccessFile f = new RandomAccessFile(file, "rw");
        c = f.getChannel();
        try {
            ByteBuffer buf = ByteBuffer.allocate(128);
            c.position(0);
            Key key = generatePBEKey(password);
            Cipher pbeCipher = getPBECipher(key);
            AlgorithmParameters params = pbeCipher.getParameters();
            byte[] paramBuf = params.getEncoded();
            byte[] encKey = pbeCipher.doFinal(getKey().getEncoded());
            c.position(0);
            buf.clear();
            buf.put((byte) paramBuf.length);
            buf.put(paramBuf);
            buf.put((byte) encKey.length);
            buf.put(encKey);
            buf.flip();
            c.write(buf);
        } catch (GeneralSecurityException e) {
            log.log(Level.WARNING, "Unable to change password", e);
            IOException ioe = new IOException("Unable to change password: " + e.getMessage());
            ioe.initCause(e);
            throw ioe;
        } finally {
            c.close();
        }
    }
