    private static long[] getFingerprint(Object idata) throws Exception {
        long[] fingerprint = (long[]) fingerprints.get(idata);
        if (fingerprint != null) {
            return fingerprint;
        }
        synchronized (lock) {
            fingerprint = (long[]) fingerprints.get(idata);
            if (fingerprint != null) {
                return fingerprint;
            }
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bfingerprint = digest.digest((byte[]) IDATA_ITS_ICODE.get(idata));
            fingerprint = new long[bfingerprint.length / 8];
            DataInputStream din = new DataInputStream(new ByteArrayInputStream(bfingerprint));
            for (int i = 0; i < fingerprint.length; i++) {
                fingerprint[i] = din.readLong();
            }
            Map newFingerprints = new WeakHashMap(fingerprints);
            newFingerprints.put(idata, fingerprint);
            fingerprints = newFingerprints;
        }
        return fingerprint;
    }
