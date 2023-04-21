    protected PropertyDataObject generate(Collection<X509Certificate> certs, BaseCertRefsData certRefsData, QualifyingProperty prop) throws PropertyDataGenerationException {
        if (null == certs) {
            throw new PropertyDataGenerationException(prop, "certificates not provided");
        }
        try {
            String digestAlgUri = this.algorithmsProvider.getDigestAlgorithmForReferenceProperties();
            MessageDigest messageDigest = this.messageDigestProvider.getEngine(digestAlgUri);
            for (X509Certificate cert : certs) {
                byte[] digestValue = messageDigest.digest(cert.getEncoded());
                certRefsData.addCertRef(new CertRef(cert.getIssuerX500Principal().getName(), cert.getSerialNumber(), digestAlgUri, digestValue));
            }
            return certRefsData;
        } catch (UnsupportedAlgorithmException ex) {
            throw new PropertyDataGenerationException(prop, ex.getMessage(), ex);
        } catch (CertificateEncodingException ex) {
            throw new PropertyDataGenerationException(prop, "cannot get encoded certificate", ex);
        }
    }
