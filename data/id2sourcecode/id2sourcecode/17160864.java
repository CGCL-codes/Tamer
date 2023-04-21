    public String computeMd5OfFirst10Frames(File mInputFile) throws java.io.FileNotFoundException, java.io.IOException, java.security.NoSuchAlgorithmException {
        int[] frameOffsets = getFrameOffsets();
        int[] frameLens = getFrameLens();
        int numFrames = frameLens.length;
        if (numFrames > 10) {
            numFrames = 10;
        }
        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        FileInputStream in = new FileInputStream(mInputFile);
        int pos = 0;
        for (int i = 0; i < numFrames; i++) {
            int skip = frameOffsets[i] - pos;
            int len = frameLens[i];
            if (skip > 0) {
                in.skip(skip);
                pos += skip;
            }
            byte[] buffer = new byte[len];
            in.read(buffer, 0, len);
            digest.update(buffer);
            pos += len;
        }
        in.close();
        byte[] hash = digest.digest();
        return bytesToHex(hash);
    }
