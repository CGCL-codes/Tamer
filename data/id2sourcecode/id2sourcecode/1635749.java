    public void extractEntryWithTmpFile(ExtZipEntry zipEntry, File outFile, String password) throws IOException, ZipException, DataFormatException {
        checkZipEntry(zipEntry);
        CentralDirectoryEntry cde = zipEntry.getCentralDirectoryEntry();
        if (!cde.isAesEncrypted()) {
            throw new ZipException("only AES encrypted files are supported");
        }
        int cryptoHeaderOffset = zipEntry.getOffset() - cde.getCryptoHeaderLength();
        byte[] salt = raFile.readByteArray(cryptoHeaderOffset, 16);
        byte[] pwVerification = raFile.readByteArray(cryptoHeaderOffset + 16, 2);
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.finest("\n" + cde.toString());
            LOG.finest("offset    = " + zipEntry.getOffset());
            LOG.finest("cryptoOff = " + cryptoHeaderOffset);
            LOG.finest("password  = " + password + " - " + password.length());
            LOG.finest("salt      = " + ByteArrayHelper.toString(salt) + " - " + salt.length);
            LOG.finest("pwVerif   = " + ByteArrayHelper.toString(pwVerification) + " - " + pwVerification.length);
        }
        decrypter.init(password, 256, salt, pwVerification);
        File tmpFile = new File(outFile.getPath() + "_TMP.zip");
        makeDir(tmpFile.getParent());
        ExtZipOutputStream zos = null;
        ZipFile zf = null;
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            zos = new ExtZipOutputStream(tmpFile);
            ExtZipEntry tmpEntry = new ExtZipEntry(zipEntry);
            tmpEntry.setPrimaryCompressionMethod(zipEntry.getMethod());
            zos.putNextEntry(tmpEntry);
            raFile.seek(cde.getOffset());
            byte[] buffer = new byte[bufferSize];
            int remaining = (int) zipEntry.getEncryptedDataSize();
            while (remaining > 0) {
                int len = (remaining > buffer.length) ? buffer.length : remaining;
                int read = raFile.readByteArray(buffer, len);
                decrypter.decrypt(buffer, read);
                zos.writeBytes(buffer, 0, read);
                remaining -= len;
            }
            zos.finish();
            zos = null;
            byte[] storedMac = new byte[10];
            raFile.readByteArray(storedMac, 10);
            byte[] calcMac = decrypter.getFinalAuthentication();
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("storedMac=" + Arrays.toString(storedMac));
                LOG.fine("calcMac=" + Arrays.toString(calcMac));
            }
            if (!Arrays.equals(storedMac, calcMac)) {
                throw new ZipException("stored authentication (mac) value does not match calculated one");
            }
            zf = new ZipFile(tmpFile);
            ZipEntry ze = zf.entries().nextElement();
            is = zf.getInputStream(ze);
            fos = new FileOutputStream(outFile.getPath());
            int read = is.read(buffer);
            while (read > 0) {
                fos.write(buffer, 0, read);
                read = is.read(buffer);
            }
        } finally {
            if (zos != null) {
                zos.close();
            }
            if (zf != null) {
                zf.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (is != null) {
                is.close();
            }
        }
        tmpFile.delete();
    }
