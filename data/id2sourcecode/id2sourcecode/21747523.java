    public static final void deployWSAR(String baseURL, String username, String password, String service, InputStream input, boolean deploy) throws IOException {
        if (!baseURL.endsWith("/")) {
            baseURL = baseURL + "/";
        }
        URL url = new URL(baseURL + "WebServiceArchive");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/octet-stream");
        connection.setRequestProperty("User-Agent", "JWSM Admin Client");
        connection.setRequestProperty("username", username);
        connection.setRequestProperty("password", password);
        connection.setRequestProperty("service", service);
        if (deploy) {
            connection.setRequestProperty("action", "deploy");
        }
        connection.setDoOutput(true);
        OutputStream output = connection.getOutputStream();
        byte[] buf = new byte[512];
        int len;
        while ((len = input.read(buf)) != -1) {
            output.write(buf, 0, len);
        }
        output.flush();
        int response = connection.getResponseCode();
        System.out.println("Response Code: " + response);
    }
