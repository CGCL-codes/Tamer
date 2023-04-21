    public static synchronized String getHashOfFile(File file) {
        if (file.exists() && file.canRead()) {
            try {
                FileInputStream stream = new FileInputStream(file);
                try {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = stream.read(buffer)) != -1) {
                        digest.update(buffer, 0, len);
                    }
                } finally {
                    stream.close();
                }
                byte[] hash = digest.digest();
                return hashToString(hash);
            } catch (Exception e) {
                XPLog.logException(Level.WARN, 0, null, e);
            }
        }
        return null;
    }
