    public static byte[] hashFile(File toHash) throws IOException {
        byte[] retBytes = null;
        FileInputStream fis = null;
        byte[] hashBytes = new byte[NUM_BYTES_TO_HASH];
        try {
            fis = new FileInputStream(toHash);
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA");
            } catch (NoSuchAlgorithmException nsae) {
                throw new IllegalStateException(nsae);
            }
            long fileLength = toHash.length();
            if (fileLength < NUM_TOTAL_HASH) {
                int numRead = 0;
                do {
                    clearHashBytes(hashBytes);
                    numRead = fis.read(hashBytes);
                    md.update(hashBytes);
                    if (toHash.length() != fileLength) throw new IOException("invalid length");
                } while (numRead == NUM_BYTES_TO_HASH);
            } else {
                long thirds = fileLength / 3;
                clearHashBytes(hashBytes);
                fis.read(hashBytes);
                md.update(hashBytes);
                if (toHash.length() != fileLength) throw new IOException("invalid length");
                clearHashBytes(hashBytes);
                fis.skip(thirds - NUM_BYTES_TO_HASH);
                fis.read(hashBytes);
                md.update(hashBytes);
                if (toHash.length() != fileLength) throw new IOException("invalid length");
                clearHashBytes(hashBytes);
                fis.skip(toHash.length() - (thirds + NUM_BYTES_TO_HASH) - NUM_BYTES_TO_HASH);
                fis.read(hashBytes);
                md.update(hashBytes);
                if (toHash.length() != fileLength) throw new IOException("invalid length");
            }
            retBytes = md.digest();
        } finally {
            if (fis != null) fis.close();
        }
        return retBytes;
    }
