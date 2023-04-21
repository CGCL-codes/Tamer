    public static SoapMessage sendSoapMessage(String serviceURL, SoapMessage sm) throws Exception {
        if (serviceURL.toLowerCase().indexOf("https") > -1) {
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }
            } };
            HostnameVerifier hv = null;
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            hv = new HostnameVerifier() {

                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
        }
        URL url = new URL(serviceURL);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "text/xml; charset='UTF-8'");
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(sm.toString());
        wr.flush();
        InputStream inputStream = conn.getInputStream();
        int length = conn.getContentLength();
        byte[] bytes;
        if (length == -1) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int c;
            while (true) {
                c = inputStream.read();
                if (c == -1) break;
                baos.write(c);
            }
            bytes = baos.toByteArray();
        } else {
            bytes = new byte[length];
            inputStream.read(bytes);
        }
        SoapMessage return_soap = new SoapMessage(new String(bytes));
        String sm_text = return_soap.getSoapBodyContent();
        if (sm_text.indexOf(":Fault") > -1) {
            if (sm.getVersion().equalsIgnoreCase(SoapMessage.V1_1)) return_soap = new SoapFaultMessage1_1(return_soap); else return_soap = new SoapFaultMessage1_2(return_soap);
        }
        return return_soap;
    }
