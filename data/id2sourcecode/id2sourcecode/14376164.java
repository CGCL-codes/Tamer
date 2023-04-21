    public static String generateFingerprint(Certificate cert, String digestAlgorithm) throws NoSuchAlgorithmException, CertificateEncodingException, IOException {
        MessageDigest md = MessageDigest.getInstance(digestAlgorithm);
        byte[] digest = md.digest(cert.getEncoded());
        ByteArrayOutputStream out = new ByteArrayOutputStream(digest.length * 2);
        new HexEncoder().encode(digest, 0, digest.length, out);
        String all = new String(out.toByteArray(), "US-ASCII").toUpperCase();
        Matcher matcher = Pattern.compile("..").matcher(all);
        StringBuffer buf = new StringBuffer();
        while (matcher.find()) {
            if (buf.length() > 0) {
                buf.append(":");
            }
            buf.append(matcher.group());
        }
        return buf.toString();
    }
