    public void open(char[] password) throws IOException {
        FileChannel c = null;
        try {
            if (!file.exists()) throw new FileNotFoundException(path);
            c = new RandomAccessFile(file, "r").getChannel();
            ByteBuffer b = ByteBuffer.allocate(256);
            c.read(b);
            b.flip();
            byte length;
            length = b.get();
            byte[] paramBuf = new byte[length];
            b.get(paramBuf);
            length = b.get();
            byte[] encKey = new byte[length];
            b.get(encKey);
            Key key = generatePBEKey(password);
            Cipher pbeCipher = getPBECipher(key, paramBuf);
            byte[] keyBuf = pbeCipher.doFinal(encKey);
            loadSymmetricKey(keyBuf);
            b.position(VERIFICATION_OFFSET);
            length = b.get();
            byte[] verification = new byte[length];
            b.get(verification);
            version = b.getInt();
            if (DATABASE_VERSION < version) {
                throw new IOException("Database version too new: " + version);
            }
            log.info("Loading database version: " + version);
            b.position(SERIAL_OFFSET);
            serialNumber = b.getLong();
            log.info("DB serial number: " + serialNumber);
            char[] verificationText = decryptText(verification);
            if (!Arrays.equals(VERIFICATION_STRING, verificationText)) {
                throw new IOException("Unable to decrypt database: verification failed: " + new String(verificationText));
            }
            loadData(c);
            saveLastKnownGood(c);
        } catch (GeneralSecurityException e) {
            log.log(Level.WARNING, "Unable to decrypt password", e);
            IOException ioe = new IOException("Unable to decrypt database");
            ioe.initCause(e);
            log.log(Level.WARNING, "Unable to decrypt database", e);
            throw ioe;
        } finally {
            if (c != null) c.close();
        }
    }
