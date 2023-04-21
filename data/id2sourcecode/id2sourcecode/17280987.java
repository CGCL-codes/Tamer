    public static void getAuth(String email, String passwd) {
        OutputStreamWriter wr = null;
        BufferedReader rd = null;
        try {
            URL url = new URL("https://www.google.com/accounts/ClientLogin");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            wr = new OutputStreamWriter(conn.getOutputStream());
            String data = "Email=" + URLEncoder.encode(email, "UTF-8") + "&Passwd=" + URLEncoder.encode(passwd, "UTF-8") + "&accountType=GOOGLE&service=ac2dm";
            wr.write(data);
            wr.flush();
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                if (line.startsWith("Auth=")) {
                    authToken = line.substring(5);
                }
            }
        } catch (IOException e) {
        } finally {
            if (wr != null) {
                try {
                    wr.close();
                } catch (IOException e) {
                }
            }
            if (rd != null) {
                try {
                    rd.close();
                } catch (IOException e) {
                }
            }
        }
    }
