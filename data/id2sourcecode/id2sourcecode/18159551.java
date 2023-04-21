    private String calcCertFingerPrint(Certificate cert) throws Exception {
        byte[] derCert = cert.getEncoded();
        MessageDigest md = MessageDigest.getInstance("SHA1");
        byte[] digest = md.digest(derCert);
        StringBuilder hexes = new StringBuilder();
        for (int eachByte = 0; eachByte < digest.length; eachByte++) {
            hexes.append(toHexDigits(digest[eachByte]));
            if (eachByte + 1 != digest.length) {
                hexes.append(':');
            }
        }
        return hexes.toString();
    }
