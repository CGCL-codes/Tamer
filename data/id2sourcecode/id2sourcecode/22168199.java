    public void fillCertInfo(CertificateInfo certInfo, Certificate certificate) {
        if (certificate instanceof X509Certificate) {
            certInfo.setCertificate((X509Certificate) certificate);
            Map<DERObjectIdentifier, String> oidMap = new HashMap<DERObjectIdentifier, String>();
            X509Certificate certX509 = (X509Certificate) certificate;
            certInfo.setAlgoPubKey(certificate.getPublicKey().getAlgorithm());
            certInfo.setAlgoSig(certX509.getSigAlgName());
            certInfo.setSignature(certX509.getSignature());
            if (certX509.getPublicKey() instanceof RSAPublicKey) {
                certInfo.setKeyLength(((RSAPublicKey) certX509.getPublicKey()).getModulus().bitLength());
                String aa = ((RSAPublicKey) certX509.getPublicKey()).getModulus().toString(16);
            }
            certInfo.setPublicKey(certX509.getPublicKey());
            certX509.getSubjectX500Principal().getName("RFC2253");
            X509Name name = new X509Name(certX509.getSubjectX500Principal().getName("RFC2253"));
            certInfo.x509NameToMap(name);
            certInfo.setKeyUsage(certX509.getKeyUsage());
            certInfo.setNotBefore(certX509.getNotBefore());
            certInfo.setNotAfter(certX509.getNotAfter());
            X509Util.getExtensions(certX509);
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                md.update(certX509.getEncoded());
                certInfo.setDigestSHA1(md.digest());
                md = MessageDigest.getInstance("SHA-256");
                md.update(certX509.getEncoded());
                certInfo.setDigestSHA256(md.digest());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (CertificateEncodingException e) {
                e.printStackTrace();
            }
        }
    }
