    public LoggedClient checkLogin(String loginName, String password) {
        OutputStreamWriter writer = null;
        BufferedReader response = null;
        try {
            String data = URLEncoder.encode(loginNameParam, charsetName) + "=" + URLEncoder.encode(loginName, charsetName);
            data += "&" + URLEncoder.encode(passwordParam, charsetName) + "=" + URLEncoder.encode(password, charsetName);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(data);
            writer.flush();
            response = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = response.readLine()) != null) {
                if (line.equalsIgnoreCase(String.valueOf(true))) return new DefaultLoggedClient(loginName); else if (line.equalsIgnoreCase(String.valueOf(false))) return null;
            }
            return null;
        } catch (IOException error) {
            return null;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ignore) {
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ignore) {
                }
            }
        }
    }
