    private Certificate[] createX509(Configuration config) throws ConfigurationException, CertificateException, IOException {
        String href = config.getAttribute("href", "");
        String data = config.getValue();
        InputStream in = null;
        try {
            if (href == null || "".equals(href)) {
                in = new ByteArrayInputStream(data.getBytes("UTF-8"));
            } else {
                URL url = new URL(href);
                in = url.openStream();
            }
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Collection certs = cf.generateCertificates(in);
            Certificate[] certificates = new Certificate[certs.size()];
            return (Certificate[]) certs.toArray(certificates);
        } finally {
            if (in != null) in.close();
        }
    }
