    public static void main(String[] args) {
        prisms.util.PrismsServiceConnector conn = new prisms.util.PrismsServiceConnector("http://localhost/WeatherEffectsServlet/prisms", "MANAGER", "Upload Test", "admin");
        conn.getConnector().setHostnameVerifier(new javax.net.ssl.HostnameVerifier() {

            public boolean verify(String hostname, javax.net.ssl.SSLSession session) {
                return true;
            }
        });
        try {
            conn.getConnector().setTrustManager(new javax.net.ssl.X509TrustManager() {

                public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws java.security.cert.CertificateException {
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws java.security.cert.CertificateException {
                }

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            });
        } catch (java.security.GeneralSecurityException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        conn.setPassword("admin");
        conn.getConnector().setFollowRedirects(Boolean.FALSE);
        java.io.OutputStream stream = null;
        java.io.FileInputStream in = null;
        try {
            conn.init();
            stream = conn.uploadData("build.xml", "text/xml", "Upload", "upload", "testName", "testValue");
            in = new java.io.FileInputStream("build.xml");
            int read = in.read();
            while (read >= 0) {
                stream.write(read);
                read = in.read();
            }
            stream.close();
            System.out.println("Upload successful");
        } catch (java.io.IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            try {
                if (in != null) in.close();
                conn.logout(true);
            } catch (java.io.IOException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }
