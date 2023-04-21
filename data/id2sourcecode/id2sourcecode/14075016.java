    private String getOld(String aUrl) {
        String response = "";
        int bytes;
        char[] buf = new char[10000];
        try {
            URL url = new URL(aUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            result = con.getResponseCode();
            if (result == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while (true) {
                    bytes = in.read(buf);
                    if (bytes == -1) {
                        break;
                    } else {
                        response += new String(buf, 0, bytes);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
