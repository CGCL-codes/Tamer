    public static final byte[] computeSHA256(byte[] arg) {
        byte[] ret = new byte[32];
        synchronized (SHA256) {
            ret = SHA256.digest(arg);
        }
        return ret;
    }
