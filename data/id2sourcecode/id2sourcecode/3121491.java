    public byte[] digest(String sAlgorithm, long lStart, long lEnd) throws IOException {
        if (!(sAlgorithm.equals("MD5") || sAlgorithm.equals("SHA-1"))) throw new IllegalArgumentException("Digest algorithm must be MD5 or SHA-1!");
        long lFilePointer = getFilePointer();
        byte[] bufDigest = null;
        try {
            MessageDigest md = MessageDigest.getInstance(sAlgorithm);
            byte[] buf = new byte[iBUFFER_SIZE];
            int iRead = 0;
            for (long lPosition = lStart; lPosition < lEnd; lPosition += iRead) {
                seek(lPosition);
                int iLength = buf.length;
                if (lEnd - lPosition < iLength) iLength = (int) (lEnd - lPosition);
                iRead = read(buf, 0, iLength);
                if (iRead != iLength) throw new IOException("Could not read " + String.valueOf(iLength) + " bytes at position " + String.valueOf(lPosition));
                md.update(buf, 0, iRead);
            }
            bufDigest = md.digest();
        } catch (NoSuchAlgorithmException nsae) {
            System.err.println(nsae.getClass().getName() + ": " + nsae.getMessage());
        }
        seek(lFilePointer);
        return bufDigest;
    }
