    public void save() throws IOException, StaleException {
        log.finest("Saving database");
        FileChannel c = null;
        try {
            ByteBuffer buf = ByteBuffer.allocate(16384);
            c = new RandomAccessFile(file, "rw").getChannel();
            if (version < DATABASE_VERSION) {
                log.info("Database version was: " + version);
                log.info("Updating database version to: " + DATABASE_VERSION);
                buf.clear();
                byte[] verification = encrypt(VERIFICATION_STRING.clone());
                c.position(VERIFICATION_OFFSET);
                buf.put((byte) verification.length);
                buf.put(verification);
                buf.putInt(DATABASE_VERSION);
                buf.flip();
                c.write(buf);
                version = DATABASE_VERSION;
            }
            c.position(SERIAL_OFFSET);
            buf.clear();
            buf.limit(8);
            c.read(buf);
            buf.flip();
            long dbSerial = buf.getLong();
            if (dbSerial != serialNumber) throw new StaleException("Serial number doesn't match, reload db");
            buf.clear();
            buf.putLong(++serialNumber);
            buf.flip();
            c.position(SERIAL_OFFSET);
            c.write(buf);
            c.position(DATA_OFFSET);
            buf.clear();
            log.finest("Saving keypairs: " + keyPairs.size());
            buf.putInt(keyPairs.size());
            log.finest("Saving folders: " + folders.size());
            buf.putInt(folders.size());
            log.finest("Saving entries: " + entries.size());
            buf.putInt(entries.size());
            buf.flip();
            c.write(buf);
            long pos = c.position();
            byte[] iv;
            try {
                Cipher symCipher = getCBCSymmetricCipher(getKey());
                AlgorithmParameters params = symCipher.getParameters();
                iv = params.getEncoded();
                for (Map.Entry<X509Certificate, PrivateKey> pair : keyPairs.entrySet()) {
                    buf.clear();
                    byte[] encrypted;
                    byte[] encoded = pair.getKey().getEncoded();
                    short len = (short) encoded.length;
                    tmp.clear();
                    tmp.putShort(len);
                    tmp.put(encoded);
                    tmp.flip();
                    encrypted = symCipher.update(tmp.array(), tmp.position(), tmp.remaining());
                    if (encrypted != null && encrypted.length > 0) buf.put(encrypted);
                    encoded = pair.getValue().getEncoded();
                    len = (short) encoded.length;
                    tmp.clear();
                    tmp.putShort(len);
                    tmp.put(encoded);
                    tmp.flip();
                    encrypted = symCipher.update(tmp.array(), tmp.position(), tmp.remaining());
                    if (encrypted != null && encrypted.length > 0) buf.put(encrypted);
                    buf.flip();
                    c.write(buf);
                }
                for (Folder f : folders) {
                    buf.clear();
                    ByteBuffer b = serialize(f);
                    byte[] encrypted = symCipher.update(b.array(), b.position(), b.remaining());
                    buf.put(encrypted);
                    buf.flip();
                    c.write(buf);
                }
                for (PasswordEntry e : entries) {
                    buf.clear();
                    ByteBuffer b = serialize(e);
                    byte[] encrypted = symCipher.update(b.array(), b.position(), b.remaining());
                    if (encrypted != null && encrypted.length > 0) buf.put(encrypted);
                    buf.flip();
                    c.write(buf);
                }
                byte[] finalBlock = symCipher.doFinal();
                if (finalBlock != null && finalBlock.length > 0) {
                    buf.clear();
                    buf.put(finalBlock);
                    buf.flip();
                    c.write(buf);
                }
            } catch (GeneralSecurityException e) {
                log.log(Level.WARNING, "Unable to encrypt database", e);
                IOException ioe = new IOException("Unable to encrypt database");
                ioe.initCause(e);
                throw ioe;
            }
            buf.clear();
            int len = (int) (c.position() - pos);
            buf.putInt(len);
            buf.flip();
            c.position(DATALEN_OFFSET);
            c.write(buf);
            buf.clear();
            byte[] encIv = encrypt(iv);
            buf.put((byte) encIv.length);
            buf.put(encIv);
            buf.flip();
            c.position(IV_OFFSET);
            c.write(buf);
            log.info("Database saved successfully");
        } finally {
            if (c != null) c.close();
        }
    }
