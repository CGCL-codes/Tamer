    public String getContacts() throws IOException {
        String output = "";
        URL url = new URL(GC_CONTACT_URL_PREFIX + URLEncoder.encode(googleSession.getEmail(), "UTF-8") + GC_CONTACT_URL_SUFFIX);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Authorization", "GoogleLogin auth=" + this.googleSession.google_token);
        conn.connect();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            output = output + line;
        }
        return output;
    }
