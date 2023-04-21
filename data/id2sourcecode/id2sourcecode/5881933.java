    protected void printCertificate(final Certificate cert) throws Exception {
        if (cert instanceof X509Certificate) {
            final X509Certificate xCert = (X509Certificate) cert;
            final byte[] encodedCert = xCert.getEncoded();
            System.out.println("Subject: " + xCert.getSubjectDN());
            System.out.println("Issuer: " + xCert.getIssuerDN());
            System.out.println("Serial: " + hexConv.fromBytes(xCert.getSerialNumber().toByteArray()));
            System.out.println("Valid not before: " + xCert.getNotBefore());
            System.out.println("Valid not after: " + xCert.getNotAfter());
            System.out.println("MD5 fingerprint: " + md5.digest(encodedCert, hexConv));
            System.out.println("SHA1 fingerprint: " + sha1.digest(encodedCert, hexConv));
        } else {
            System.out.println(cert);
        }
    }
