    public boolean verifyPassword(char[] password) throws FileNotFoundException, StaleException {
        FileChannel c = null;
        try {
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
            char[] verificationText = decryptText(verification);
            if (!Arrays.equals(VERIFICATION_STRING, verificationText)) {
                throw new IOException("Unable to decrypt database: verification failed: " + new String(verificationText));
            }
            b.position(SERIAL_OFFSET);
            long dbSerial = b.getLong();
            if (dbSerial != serialNumber) {
                throw new StaleException("Serial number doesn't match, refresh database");
            }
            return true;
        } catch (GeneralSecurityException e) {
            log.log(Level.WARNING, "Unable to decrypt database", e);
        } catch (FileNotFoundException e) {
            log.log(Level.WARNING, "Underlying database file is missing", e);
            throw e;
        } catch (IOException e) {
            log.log(Level.SEVERE, "IO Error: " + e.getMessage(), e);
        } finally {
            try {
                if (c != null) c.close();
            } catch (IOException e) {
            }
        }
        return false;
    }
