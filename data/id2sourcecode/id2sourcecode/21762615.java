    public static String getCustomUsername(String sessionValidatorURL, String sessionName, String sessionIdValue) {
        try {
            URL url;
            URLConnection urlConn;
            DataOutputStream printout;
            DataInputStream input;
            url = new URL(sessionValidatorURL);
            urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            printout = new DataOutputStream(urlConn.getOutputStream());
            String content = new String();
            content = sessionName + "=" + sessionIdValue;
            printout.writeBytes(content);
            printout.flush();
            printout.close();
            input = new DataInputStream(urlConn.getInputStream());
            String str;
            String username = new String();
            while (null != ((str = input.readLine()))) {
                username = username + str;
            }
            input.close();
            return username;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
