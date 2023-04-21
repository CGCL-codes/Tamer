    public long pseudoRandomFunction() {
        byte[] byteSeed = new byte[6];
        for (int j = 0; j < 6; j++) {
            byte[] idBytes = getID().toByteArray();
            byte[] indexBytes = getIndex().toByteArray();
            byte[] inputBytes = new byte[idBytes.length + indexBytes.length];
            System.arraycopy(idBytes, 0, inputBytes, 0, idBytes.length);
            System.arraycopy(indexBytes, 0, inputBytes, idBytes.length, indexBytes.length);
            try {
                MessageDigest algorithm = MessageDigest.getInstance("MD5");
                algorithm.reset();
                algorithm.update(inputBytes);
                byte[] messageDigest = algorithm.digest();
                byteSeed[j] = messageDigest[messageDigest.length - 1];
            } catch (NoSuchAlgorithmException e) {
                LogUtil.logError(e);
            }
            incrementIndex();
        }
        long longSeed = 0;
        for (int j = 0; j < byteSeed.length; j++) {
            longSeed = (longSeed << 8) + (byteSeed[j] & 0xff);
        }
        return longSeed;
    }
