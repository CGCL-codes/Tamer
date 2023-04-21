    private static String getFingerprint(final X509Certificate c, final MessageDigest d) throws CertificateException {
        final byte[] encodedCertBytes = c.getEncoded();
        final byte[] digestBytes;
        synchronized (d) {
            digestBytes = d.digest(encodedCertBytes);
        }
        final StringBuilder buffer = new StringBuilder(3 * encodedCertBytes.length);
        toHex(digestBytes, ":", buffer);
        return buffer.toString();
    }
