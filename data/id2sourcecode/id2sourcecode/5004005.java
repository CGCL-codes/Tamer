    public ReloadableX509CRL(String crlUrl, CertificateFactory certificateFactory) {
        this.crlUrl = crlUrl;
        if (certificateFactory == null) {
            try {
                this.certificateFactory = CertificateFactory.getInstance("X.509");
            } catch (CertificateException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.certificateFactory = certificateFactory;
        }
        this.reloaderCallable = new Callable<X509CRL>() {

            public X509CRL call() throws Exception {
                InputStream is = null;
                X509CRL crl = null;
                try {
                    URL url = new URL(ReloadableX509CRL.this.crlUrl);
                    is = url.openStream();
                    crl = (X509CRL) ReloadableX509CRL.this.certificateFactory.generateCRL(is);
                    ReloadableX509CRL.this.crl = crl;
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
                return crl;
            }
        };
    }
