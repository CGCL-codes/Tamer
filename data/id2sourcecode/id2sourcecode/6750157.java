    public String getKeyFingerprint() throws Exception {
        Logger.infoMessage("Creating key digest");
        MessageDigest mesdig;
        mesdig = MessageDigest.getInstance("MD5");
        byte[] result = mesdig.digest(getPublicKey().getEncoded());
        String fingerprint = new String("");
        for (int jj = 0; jj < result.length; ++jj) {
            char bytes[] = new char[2];
            bytes[0] = "0123456789ABCDEF".charAt((result[jj] >> 4) & 0x0F);
            bytes[1] = "0123456789ABCDEF".charAt(result[jj] & 0x0F);
            fingerprint = fingerprint.concat(new String(bytes) + ":");
        }
        return fingerprint;
    }
