    private HttpResponse connect() throws IOException {
        URL url = new URL(this.urlLocation);
        con = (HttpURLConnection) url.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.setDefaultUseCaches(false);
        con.setAllowUserInteraction(true);
        if (authenticator != null) {
            authenticator.authenticate(con);
        }
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        String date = format.format(new Date());
        con.setRequestProperty("Date", date);
        con.setRequestMethod(method);
        if (this.contentType != null) {
            con.setRequestProperty("content-type", contentType);
        }
        if (this.acceptType != null) {
            con.setRequestProperty("accept", acceptType);
        }
        con.connect();
        if (con.getResponseCode() == 200) {
            return null;
        } else {
            if (con.getResponseCode() == 401) {
                String authType = con.getHeaderField("WWW-Authenticate");
                if (authType.contains("Basic")) {
                    return new HttpResponse(true, "Basic", "Test");
                }
            }
            return null;
        }
    }
