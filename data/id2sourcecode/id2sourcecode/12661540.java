    public static String post(String urlStr, String content, String type, String username, String password) throws Exception {
        URL url = new URL(urlStr);
        URLConnection urlConn = url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        if (type.equals("xml")) {
            urlConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        } else if (type.equals("form")) {
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        }
        if (username != null && password != null) {
            String auth = "Auth=" + username + ":" + password;
            urlConn.setRequestProperty("Cookie", auth);
        }
        DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());
        printout.writeBytes(content);
        printout.flush();
        printout.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        String str;
        String response = "";
        while (null != (str = input.readLine())) {
            response += str;
        }
        input.close();
        return response;
    }
