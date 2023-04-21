    protected byte[] encode(byte[] bytes) {
        if (encoding_type == ET_SHA1) {
            SHA1Hasher hasher = new SHA1Hasher();
            return (hasher.calculateHash(bytes));
        } else if (encoding_type == ET_MD5) {
            try {
                return (MessageDigest.getInstance("md5").digest(bytes));
            } catch (Throwable e) {
                Debug.printStackTrace(e);
            }
        }
        return (bytes);
    }
