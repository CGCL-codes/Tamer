    private boolean isSigValid(int styp, long off, long len, byte[] data) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest;
        if (styp == 0) {
            digest = MessageDigest.getInstance("MD5");
        } else {
            digest = MessageDigest.getInstance("SHA-1");
        }
        try {
            _raf.seek(off);
            int buflen = (len < 65536 ? (int) len : 65536);
            byte[] buf = new byte[buflen];
            while (len > 0) {
                int btr = (len < buflen ? (int) len : buflen);
                int bytesRead = _raf.read(buf, 0, btr);
                digest.update(buf, 0, bytesRead);
            }
            byte[] digestVal = digest.digest();
            if (digestVal.length != data.length) {
                return false;
            }
            for (int i = 0; i < data.length; i++) {
                if (digestVal[i] != data[i]) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
