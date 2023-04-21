    public void httpConnection(String toSend) {
        try {
            URL url = new URL(defaultUrl + extUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            if (reqType) conn.setRequestMethod("POST"); else conn.setRequestMethod("GET");
            String userPassword = usr + ":" + psw;
            Base64Encoder be = new Base64Encoder();
            String encoding = be.encode(userPassword);
            conn.setRequestProperty("Connection", "close");
            conn.setRequestProperty("Authorization", "Basic " + encoding);
            if (reqType) {
                os = conn.getOutputStream();
                if (toSend != "") {
                    byte data[];
                    data = (toSend).getBytes();
                    os.write(data);
                }
            }
            is = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
