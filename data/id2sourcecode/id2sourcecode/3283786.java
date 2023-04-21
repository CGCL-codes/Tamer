    public static String getTokenFromServer(String email, String password) throws IOException {
        NamespaceManager.set(Util.ADMIN_NAMESPACE);
        StringBuilder builder = new StringBuilder();
        builder.append("Email=").append(email);
        builder.append("&Passwd=").append(password);
        builder.append("&accountType=GOOGLE");
        builder.append("&source=DDDForAndroid");
        builder.append("&service=ac2dm");
        byte[] data = builder.toString().getBytes();
        URL url = new URL(AUTHENTICATION_ENDPOINT);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Content-Length", Integer.toString(data.length));
        OutputStream output = con.getOutputStream();
        output.write(data);
        output.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line = null;
        String auth_key = null;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Auth=")) {
                auth_key = line.substring(5);
            }
        }
        Util.updateToken(auth_key);
        return auth_key;
    }
