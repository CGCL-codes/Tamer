    private void setTwitter(String username, String password, String message) {
        URL url = null;
        try {
            url = new URL("https://twitter.com/statuses/update.xml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection connection = null;
        try {
            connection = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        String authorization = username + ":" + password;
        Base64 en = new Base64();
        String encoded = new String(en.encode(authorization.getBytes())).trim();
        connection.setRequestProperty("Authorization", "Basic " + encoded);
        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(connection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.write("status=" + URLEncoder.encode(message, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String response;
        try {
            while ((response = in.readLine()) != null) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
