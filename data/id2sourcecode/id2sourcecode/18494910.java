    public static Document askForCredential(Config config, String credential_xml, String IDVotazione) {
        String surl = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String sconn = config.getSconn();
            Connection conn = DriverManager.getConnection(sconn);
            String query = "" + " SELECT authserver FROM votazioni " + " WHERE ID=? ";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, IDVotazione);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                surl = rs.getString(1);
                LOGGER.debug("authserver by db " + surl);
            } else return null;
        } catch (SQLException ex) {
            LOGGER.error("SQLException", ex);
            return null;
        } catch (ClassNotFoundException ex) {
            LOGGER.error("ClassNotFoundException", ex);
            return null;
        }
        String response = "";
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }
        } };
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (java.security.KeyManagementException e) {
            LOGGER.error("Error opening ssl connection to authserver", e);
            return null;
        } catch (java.security.NoSuchAlgorithmException e) {
            LOGGER.error("Error opening ssl connection to authserver", e);
            return null;
        }
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            });
            URL url = new URL(surl);
            String test = new String(Base64.encodeBase64(credential_xml.getBytes()));
            LOGGER.debug(test);
            String data = "data=" + URLEncoder.encode(test, "UTF-8");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                response += line;
            }
            wr.close();
            rd.close();
        } catch (MalformedURLException e) {
            LOGGER.error("Malformed authserver url", e);
            return null;
        } catch (java.io.IOException e) {
            LOGGER.error("IO Error while connected to authserver", e);
            return null;
        }
        LOGGER.info("Received response from authserver at " + surl);
        if (LOGGER.isDebugEnabled()) LOGGER.debug("response:" + response);
        SAXReader reader = new SAXReader();
        Document credential = null;
        try {
            ByteArrayInputStream iv = new ByteArrayInputStream(response.getBytes());
            credential = reader.read(iv);
        } catch (DocumentException e) {
            LOGGER.error("Error reading authserver response", e);
            return null;
        }
        if (LOGGER.isTraceEnabled()) LOGGER.trace(credential.asXML());
        boolean hasChildren = false;
        for (Iterator i = credential.getRootElement().elementIterator(); i.hasNext(); ) {
            hasChildren = true;
            break;
        }
        if (!hasChildren) {
            LOGGER.warn("user not found");
            return null;
        }
        return credential;
    }
