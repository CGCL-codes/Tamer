    public static void main(String[] args) {
        try {
            System.getProperties().put("proxySet", "true");
            System.getProperties().put("proxyHost", "proxy");
            System.getProperties().put("proxyPort", "8080");
            Base64Encoder encoder64 = new Base64Encoder();
            URL url = new URL("http://www.efrei.fr/");
            URLConnection connection = url.openConnection();
            String password = "user:passwd";
            String encodedPassword = "Basic " + encoder64.encode(password);
            connection.setRequestProperty("Proxy-Authorization", encodedPassword);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String test;
            while ((test = in.readLine()) != null) {
                System.err.println(test);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
