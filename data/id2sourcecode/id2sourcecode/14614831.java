    @Override
    public void exec(String name, Galaxy galaxy) {
        serverID = OGSserver.getProperty("Racelist.Server");
        gameTypePrefixes = Utils.split(OGSserver.getProperty("Racelist.GameTypes", ""));
        gameTypeNames = new String[gameTypePrefixes.length];
        for (int i = 0; i < gameTypePrefixes.length; i++) {
            String[] strings = gameTypePrefixes[i].split(":", 2);
            gameTypePrefixes[i] = strings[0];
            if (strings.length > 1) gameTypeNames[i] = strings[1]; else gameTypeNames[i] = strings[0];
        }
        String charset = OGSserver.getProperty("Racelist.Charset", "UTF-8");
        try {
            URL url = new URL(OGSserver.getProperty("Action." + name + ".URL"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String userInfo = url.getUserInfo();
            if (userInfo == null) {
                String user = OGSserver.getProperty("Racelist.User");
                if (user != null) {
                    userInfo = user;
                    String password = OGSserver.getProperty("Racelist.Password");
                    if (password != null) userInfo += ':' + password;
                }
            }
            if (userInfo != null) conn.setRequestProperty("Authorization", "Basic " + new String(BASE64.encode(userInfo.getBytes())));
            conn.setConnectTimeout(TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            Map<String, String> params = new HashMap<String, String>();
            fillForm(params, galaxy);
            OutputStream out = conn.getOutputStream();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (!first) out.write('&'); else first = false;
                out.write(URLEncoder.encode(entry.getKey(), charset).getBytes("US-ASCII"));
                out.write('=');
                out.write(URLEncoder.encode(entry.getValue(), charset).getBytes("US-ASCII"));
            }
            out.flush();
            out.close();
            conn.connect();
            conn.getResponseCode();
            Galaxy.getLogger().fine("Request result: " + conn.getResponseMessage());
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Galaxy.getLogger().log(Level.WARNING, "Can't update RaceList. Response: " + conn.getResponseCode() + ' ' + conn.getResponseMessage());
            }
            conn.disconnect();
        } catch (IOException err) {
            Galaxy.getLogger().log(Level.WARNING, "Can't update RaceList", err);
        }
    }
