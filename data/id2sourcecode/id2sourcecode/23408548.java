    private Certificate[] createPKCS7(Configuration config) throws Exception {
        String href = config.getAttribute("href");
        InputStream in = null;
        try {
            URL url = new URL(href);
            in = url.openStream();
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Collection certs = cf.generateCertificates(in);
            Certificate[] certificates = new Certificate[certs.size()];
            return (Certificate[]) certs.toArray(certificates);
        } finally {
            if (in != null) in.close();
        }
    }
