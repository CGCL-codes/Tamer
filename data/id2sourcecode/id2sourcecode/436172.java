    public static void main(String[] args) {
        String cuki = new String();
        try {
            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            URL url = new URL("https://www.gmail.com");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            String cookieHeader = connection.getHeaderField("set-cookie");
            if (cookieHeader != null) {
                int index = cookieHeader.indexOf(";");
                if (index >= 0) {
                    cuki = cookieHeader.substring(0, index);
                }
                connection.setRequestProperty("Cookie", cuki);
            }
            connection.setRequestMethod("POST");
            connection.setFollowRedirects(true);
            String query = "UserID=" + URLEncoder.encode("williamalex@hotmail.com");
            query += "&";
            query += "password=" + URLEncoder.encode("password");
            query += "&";
            query += "UserChk=" + URLEncoder.encode("Bidder");
            query += "&";
            query += "PreviousURL=" + URLEncoder.encode("https://www.anysecuresite.com.sg/auct.cfm");
            connection.setRequestProperty("Content-length", String.valueOf(query.length()));
            connection.setRequestProperty("Content-Type", "application/x-www- form-urlencoded");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows 98; DigExt)");
            DataOutputStream output = new DataOutputStream(connection.getOutputStream());
            int queryLength = query.length();
            output.writeBytes(query);
            System.out.println("Resp Code:" + connection.getResponseCode());
            System.out.println("Resp Message:" + connection.getResponseMessage());
            DataInputStream input = new DataInputStream(connection.getInputStream());
            for (int c = input.read(); c != -1; c = input.read()) {
                System.out.print((char) c);
            }
            input.close();
        } catch (Exception e) {
            System.out.println("Something bad just happened.");
            System.out.println(e);
            e.printStackTrace();
        }
    }
