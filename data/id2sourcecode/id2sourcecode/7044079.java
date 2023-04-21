    public static byte[] secureHash(Certificate[] certificates) {
        byte[] secureHash = null;
        long beg = SystemClock.uptimeMillis();
        if (certificates != null && certificates.length != 0) {
            byte[] encodedCertPath = null;
            try {
                synchronized (CertificateValidatorCache.class) {
                    if (sCertificateFactory == null) {
                        try {
                            sCertificateFactory = CertificateFactory.getInstance("X.509");
                        } catch (GeneralSecurityException e) {
                            if (HttpLog.LOGV) {
                                HttpLog.v("CertificateValidatorCache:" + " failed to create the certificate factory");
                            }
                        }
                    }
                }
                CertPath certPath = sCertificateFactory.generateCertPath(Arrays.asList(certificates));
                if (certPath != null) {
                    encodedCertPath = certPath.getEncoded();
                    if (encodedCertPath != null) {
                        Sha1MessageDigest messageDigest = new Sha1MessageDigest();
                        secureHash = messageDigest.digest(encodedCertPath);
                    }
                }
            } catch (GeneralSecurityException e) {
            }
        }
        long end = SystemClock.uptimeMillis();
        mCost += (end - beg);
        return secureHash;
    }
