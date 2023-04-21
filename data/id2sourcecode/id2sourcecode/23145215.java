    public void set(String name, Object obj) throws CertificateException, IOException {
        if (readOnly) throw new CertificateException("cannot over-write existing" + " certificate");
        X509AttributeName attr = new X509AttributeName(name);
        String id = attr.getPrefix();
        if (!(id.equalsIgnoreCase(NAME))) {
            throw new CertificateException("Invalid root of attribute name," + " expected [" + NAME + "], received " + id);
        }
        attr = new X509AttributeName(attr.getSuffix());
        id = attr.getPrefix();
        if (id.equalsIgnoreCase(INFO)) {
            if (attr.getSuffix() == null) {
                if (!(obj instanceof X509CertInfo)) {
                    throw new CertificateException("Attribute value should" + " be of type X509CertInfo.");
                }
                info = (X509CertInfo) obj;
                signedCert = null;
            } else {
                info.set(attr.getSuffix(), obj);
                signedCert = null;
            }
        } else {
            throw new CertificateException("Attribute name not recognized or " + "set() not allowed for the same: " + id);
        }
    }
