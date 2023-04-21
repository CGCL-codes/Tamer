    public void decode(InputStream in) throws CertificateException, IOException {
        if (this.readOnly) throw new CertificateException("Unable to overwrite certificate"); else {
            in = Markable(in);
            try {
                in.mark(Integer.MAX_VALUE);
                DerValue der = readRFC1421Cert(in);
                this.parse(der);
            } catch (IOException ioe) {
                in.reset();
                DerValue der = new DerValue(in);
                this.parse(der);
            }
        }
    }
