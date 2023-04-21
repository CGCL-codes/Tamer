    public boolean match(Certificate cert) {
        if (!(cert instanceof X509Certificate)) {
            return false;
        }
        X509Certificate x509Cert = (X509Certificate) cert;
        try {
            if (holder.getBaseCertificateID() != null) {
                return holder.getBaseCertificateID().getSerial().getValue().equals(x509Cert.getSerialNumber()) && matchesDN(PrincipalUtil.getIssuerX509Principal(x509Cert), holder.getBaseCertificateID().getIssuer());
            }
            if (holder.getEntityName() != null) {
                if (matchesDN(PrincipalUtil.getSubjectX509Principal(x509Cert), holder.getEntityName())) {
                    return true;
                }
            }
            if (holder.getObjectDigestInfo() != null) {
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance(getDigestAlgorithm(), "BC");
                } catch (Exception e) {
                    return false;
                }
                switch(getDigestedObjectType()) {
                    case ObjectDigestInfo.publicKey:
                        md.update(cert.getPublicKey().getEncoded());
                        break;
                    case ObjectDigestInfo.publicKeyCert:
                        md.update(cert.getEncoded());
                        break;
                }
                if (!Arrays.areEqual(md.digest(), getObjectDigest())) {
                    return false;
                }
            }
        } catch (CertificateEncodingException e) {
            return false;
        }
        return false;
    }
