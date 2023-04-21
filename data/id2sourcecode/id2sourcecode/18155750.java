    public void authorize(String code) throws JSONException, IOException {
        StringBuilder urlBuilder = new StringBuilder().append(protocol).append("://").append(host).append(':').append(port).append(accessTokenPath);
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes("client_id=" + clientId + "&client_secret=" + clientSecret + "&code=" + code + "&grant_type=authorization_code&redirect_uri=" + redirectUrl);
        outputStream.flush();
        outputStream.close();
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = inputStream.readLine()) != null) {
            response.append(line);
        }
        inputStream.close();
        JSONObject responseObject = new JSONObject(response.toString());
        setoAuthToken(responseObject.getString("access_token"));
    }
