    static void checkCertRef(CertRef certRef, X509Certificate cert, MessageDigestEngineProvider messageDigestProvider) throws InvalidCertRefException {
        MessageDigest messageDigest;
        Throwable t = null;
        try {
            messageDigest = messageDigestProvider.getEngine(certRef.digestAlgUri);
            byte[] actualDigest = messageDigest.digest(cert.getEncoded());
            if (!Arrays.equals(certRef.digestValue, actualDigest)) throw new InvalidCertRefException("digests mismatch");
            return;
        } catch (UnsupportedAlgorithmException ex) {
            t = ex;
        } catch (CertificateEncodingException ex) {
            t = ex;
        }
        throw new InvalidCertRefException(t.getMessage());
    }
