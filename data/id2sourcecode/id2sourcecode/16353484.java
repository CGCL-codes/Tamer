    public void testSAMLR2AuthnToJOSSO11() throws Exception {
        String location = null;
        URL spUrl = new URL("http://localhost:8181/JOSSO11/BIND/CH2?SAMLRequest=" + BASE64_SAMLR2_AUTHNREQUEST);
        HttpURLConnection urlConn = (HttpURLConnection) spUrl.openConnection();
        urlConn.setInstanceFollowRedirects(false);
        urlConn.connect();
        String headerName = null;
        String jossoSession = null;
        for (int i = 1; (headerName = urlConn.getHeaderFieldKey(i)) != null; i++) {
            if (headerName.equals("Location")) {
                location = urlConn.getHeaderField(i);
            }
        }
        assert location != null;
    }
