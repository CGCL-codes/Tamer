    public static String generateKeyFromSite(String theSite, CryptnosApplication theApp) {
        try {
            if (theSite != null && theSite.length() > 0) {
                MessageDigest hasher = MessageDigest.getInstance("SHA-512");
                return base64String(hasher.digest(theSite.concat("android_id").getBytes(theApp.getTextEncoding())));
            } else return theSite;
        } catch (Exception e) {
            return theSite;
        }
    }
