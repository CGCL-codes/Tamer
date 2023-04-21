    public static Hash computeHash(InputStream stream, HashPair pair) {
        byte[] retr = null;
        try {
            MessageDigest md = MessageDigest.getInstance(pair.hashType);
            md.reset();
            int count = 0;
            byte[] bytes = new byte[BUFFER_CHUNK_MAX];
            while ((count = stream.read(bytes)) > 0) {
                if (count == BUFFER_CHUNK_MAX) md.update(bytes); else md.update(bytes, 0, count);
            }
            retr = md.digest();
        } catch (Throwable t) {
            return new NullHash();
        }
        Hash hash;
        try {
            hash = pair.hashClass.newInstance();
        } catch (Throwable t) {
            logger.error("Could not generate a hash instance of " + pair.hashClass, t);
            return NULL_HASH;
        }
        hash.setHashBytes(retr);
        return hash;
    }
