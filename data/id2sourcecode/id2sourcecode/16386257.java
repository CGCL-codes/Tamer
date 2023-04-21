    public static final synchronized byte[] getRawUID() {
        try {
            if (c_ReseedCounter == RANDOM_RESEED) {
                seedRandom();
                c_ReseedCounter = 0;
            } else {
                c_ReseedCounter++;
            }
            c_RndGen.nextBytes(UID_LENGTH, 0, c_Buffer);
            longToBytes(System.currentTimeMillis(), c_Buffer, 21);
            System.arraycopy(c_SpatialBytes, 0, c_Buffer, 52, c_SpatialBytes.length);
            return MD5.digest(c_Buffer);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        return null;
    }
