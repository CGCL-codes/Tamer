    private String digest(String filename, MessageDigest digest, long start, long end) throws IOException {
        File file = resolveFile(filename);
        if (!file.exists() && filename.startsWith("\"") && filename.endsWith("\"")) {
            file = resolveFile(filename.substring(1, filename.length() - 1));
        }
        FileInputStream in = null;
        try {
            byte[] buf = new byte[8192];
            int len;
            if (start == 0 && end == 0) {
                in = new FileInputStream(file);
            } else {
                in = new LimitedFileInputStream(file, start, end);
            }
            while ((len = in.read(buf, 0, buf.length)) != -1) {
                digest.update(buf, 0, len);
            }
            return new String(Hex.bytesToHex(digest.digest()));
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Logging.getErrorLog().reportException("Failed to close file input stream", e);
                }
            }
        }
    }
