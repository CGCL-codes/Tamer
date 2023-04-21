    public static String getGlobalDeviceId(final Context appContext) {
        String systemId = System.getString(appContext.getContentResolver(), System.ANDROID_ID);
        if (systemId == null || systemId.toLowerCase().equals(DROID2_ANDROID_ID)) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(systemId.getBytes());
            BigInteger hashedNumber = new BigInteger(1, digest);
            return new String(hashedNumber.toString(16));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
