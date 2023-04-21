    public static byte[] hash(InputStream in, long bytesToRead, String algorithm) throws NoSuchAlgorithmException, IOException {
        if (bytesToRead < 0 && bytesToRead != -1) throw new IllegalArgumentException("bytesToRead < 0 && bytesToRead != -1");
        long bytesReadTotal = 0;
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] data = new byte[10240];
        while (true) {
            int len;
            if (bytesToRead < 0) len = data.length; else {
                len = (int) Math.min(data.length, bytesToRead - bytesReadTotal);
                if (len < 1) break;
            }
            int bytesRead = in.read(data, 0, len);
            if (bytesRead < 0) {
                if (bytesToRead >= 0) throw new IOException("Unexpected EndOfStream! bytesToRead==" + bytesToRead + " but only " + bytesReadTotal + " bytes could be read from InputStream!");
                break;
            }
            bytesReadTotal += bytesRead;
            if (bytesRead > 0) md.update(data, 0, bytesRead);
        }
        return md.digest();
    }
