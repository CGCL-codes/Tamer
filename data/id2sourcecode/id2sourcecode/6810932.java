    static String calcCertFingerPrint(X509Certificate cert) throws Exception {
        byte[] derCert = cert.getEncoded();
        MessageDigest md = MessageDigest.getInstance("SHA1");
        byte[] digest = md.digest(derCert);
        StringBuffer hexes = new StringBuffer();
        for (int eachByte = 0; eachByte < digest.length; eachByte++) {
            hexes.append(toHexDigits(digest[eachByte]));
            if (eachByte + 1 != digest.length) {
                hexes.append(':');
            }
        }
        return hexes.toString();
    }
