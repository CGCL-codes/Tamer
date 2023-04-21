    public void delete(String name) throws CertificateException, IOException {
        if (readOnly) throw new CertificateException("cannot over-write existing" + " certificate");
        X509AttributeName attr = new X509AttributeName(name);
        String id = attr.getPrefix();
        if (!(id.equalsIgnoreCase(NAME))) {
            throw new CertificateException("Invalid root of attribute name," + " expected [" + NAME + "], received " + id);
        }
        attr = new X509AttributeName(attr.getSuffix());
        id = attr.getPrefix();
        if (id.equalsIgnoreCase(INFO)) {
            if (attr.getSuffix() != null) {
                info = null;
            } else {
                info.delete(attr.getSuffix());
            }
        } else if (id.equalsIgnoreCase(ALG_ID)) {
            algId = null;
        } else if (id.equalsIgnoreCase(SIGNATURE)) {
            signature = null;
        } else if (id.equalsIgnoreCase(SIGNED_CERT)) {
            signedCert = null;
        } else {
            throw new CertificateException("Attribute name not recognized or " + "delete() not allowed for the same: " + id);
        }
    }
