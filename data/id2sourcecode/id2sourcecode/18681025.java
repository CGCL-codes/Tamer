    private X509CRL getCRL(String location) throws CertPathReviewerException {
        X509CRL result = null;
        try {
            URL url = new URL(location);
            if (url.getProtocol().equals("http") || url.getProtocol().equals("https")) {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.connect();
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
                    result = (X509CRL) cf.generateCRL(conn.getInputStream());
                } else {
                    throw new Exception(conn.getResponseMessage());
                }
            }
        } catch (Exception e) {
            ErrorBundle msg = new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.loadCrlDistPointError", new Object[] { new UntrustedInput(location), e.getMessage(), e, e.getClass().getName() });
            throw new CertPathReviewerException(msg);
        }
        return result;
    }
