    public static void main(String[] args) {
        try {
            String keystore = new File(HttpsTest.class.getClassLoader().getResource("clientkeystore").toURI()).toString();
            System.setProperty("javax.net.ssl.trustStore", keystore);
            System.setProperty("javax.net.ssl.trustStorePassword", "justAtest");
            final String login = "me";
            final String password = "pass";
            Authenticator.setDefault(new Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(login, password.toCharArray());
                }
            });
            System.setProperty("java.net.useSystemProxies", "true");
            URL url = new URL("https://www.enitsys.net");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader i = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String s = i.readLine();
            while (s != null) {
                System.out.println(s);
                s = i.readLine();
            }
            i.close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
