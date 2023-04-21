    private void getAuthenticationToken() throws Exception {
        log4j.debug("Attempting to retrieve Google auth ticket");
        String data = URLEncoder.encode("accountType", "UTF-8") + "=" + URLEncoder.encode(GC_ACCOUNT_TYPE, "UTF-8");
        data += "&" + URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(this.email, "UTF-8");
        data += "&" + URLEncoder.encode("Passwd", "UTF-8") + "=" + URLEncoder.encode(this.password, "UTF-8");
        data += "&" + URLEncoder.encode("service", "UTF-8") + "=" + URLEncoder.encode(GC_SERVICE, "UTF-8");
        data += "&" + URLEncoder.encode("source", "UTF-8") + "=" + URLEncoder.encode(GC_SOURCE, "UTF-8");
        URL url = new URL(GC_AUTHENTICATION_URL);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, "=");
            while (st.hasMoreTokens()) {
                String s = st.nextToken();
                if (s.equals("Auth")) {
                    log4j.debug("google session retrieved successfully");
                    this.google_token = st.nextToken();
                    break;
                }
            }
        }
        wr.close();
        rd.close();
    }
