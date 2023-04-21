    public static void main(String[] args) {
        String configName = "c:\\eclipse\\workspace\\hardtokenmgmt\\pkcs11.cfg";
        Provider p = new sun.security.pkcs11.SunPKCS11(configName);
        Security.addProvider(p);
        try {
            System.setProperty("javax.net.ssl.keyStoreType", "PKCS11");
            System.setProperty("javax.net.ssl.keyStore", "NONE");
            System.setProperty("javax.net.ssl.trustStore", "c:\\trust.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "6631");
            System.setProperty("javax.net.ssl.trustStorePassword", "foo123");
            KeyStore ks = KeyStore.getInstance("PKCS11");
            ks.load(null, "6631".toCharArray());
            String httpsURL = "https://localhost:8443/";
            URL url = new URL(httpsURL);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            InputStream ins = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            while ((inputLine = in.readLine()) != null) System.out.println(inputLine);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
