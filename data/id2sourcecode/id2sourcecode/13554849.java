    public static void main(String[] args) throws Exception {
        String host;
        int port;
        char[] passphrase;
        if ((args.length == 1) || (args.length == 2)) {
            String[] c = args[0].split(":");
            host = c[0];
            port = (c.length == 1) ? 443 : Integer.parseInt(c[1]);
            String p = (args.length == 1) ? "changeit" : args[1];
            passphrase = p.toCharArray();
        } else {
            System.out.println("Usage: java InstallCert <host>[:port] [passphrase]");
            return;
        }
        File file = new File("jssecacerts");
        if (file.isFile() == false) {
            char SEP = File.separatorChar;
            File dir = new File(System.getProperty("java.home") + SEP + "lib" + SEP + "security");
            file = new File(dir, "jssecacerts");
            if (file.isFile() == false) {
                file = new File(dir, "cacerts");
            }
        }
        System.out.println("Loading KeyStore " + file + "...");
        InputStream in = new FileInputStream(file);
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(in, passphrase);
        in.close();
        SSLContext context = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
        context.init(null, new TrustManager[] { tm }, null);
        final SSLSocketFactory factory = context.getSocketFactory();
        System.out.println("Opening connection to " + host + ":" + port + "...");
        SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
        socket.setSoTimeout(10000);
        try {
            System.out.println("Starting SSL handshake...");
            socket.startHandshake();
            socket.close();
            System.out.println();
            System.out.println("No errors, certificate is already trusted");
        } catch (SSLException e) {
            System.out.println();
            e.printStackTrace(System.out);
        }
        X509Certificate[] chain = tm.chain;
        if (chain == null) {
            System.out.println("Could not obtain server certificate chain");
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println();
        System.out.println("Server sent " + chain.length + " certificate(s):");
        System.out.println();
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        for (int i = 0; i < chain.length; i++) {
            X509Certificate cert = chain[i];
            System.out.println(" " + (i + 1) + " Subject " + cert.getSubjectDN());
            System.out.println("   Issuer  " + cert.getIssuerDN());
            sha1.update(cert.getEncoded());
            System.out.println("   sha1    " + toHexString(sha1.digest()));
            md5.update(cert.getEncoded());
            System.out.println("   md5     " + toHexString(md5.digest()));
            System.out.println();
        }
        System.out.println("Enter certificate to add to trusted keystore or 'q' to quit: [1]");
        String line = reader.readLine().trim();
        int k;
        try {
            k = (line.length() == 0) ? 0 : Integer.parseInt(line) - 1;
        } catch (NumberFormatException e) {
            System.out.println("KeyStore not changed");
            return;
        }
        X509Certificate cert = chain[k];
        String alias = host + "-" + (k + 1);
        ks.setCertificateEntry(alias, cert);
        OutputStream out = new FileOutputStream("jssecacerts");
        ks.store(out, passphrase);
        out.close();
        System.out.println();
        System.out.println(cert);
        System.out.println();
        System.out.println("Added certificate to keystore 'jssecacerts' using alias '" + alias + "'");
        URL url = new URL("https://" + args[0] + "/login/login.cgi?" + "userType=1&" + "name=&" + "passwd=&" + "btn_submit=enter&" + "login_action=login&" + "path=&" + "restart=no&" + "systemfull=no");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        HttpsURLConnectionImpl ssl = (HttpsURLConnectionImpl) http;
        ssl.setSSLSocketFactory(factory);
        ssl.setHostnameVerifier(new HostnameVerifier() {

            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        http.setDoOutput(true);
        http.setDoInput(true);
        http.setAllowUserInteraction(true);
        int responseCode = http.getResponseCode();
        BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
        String tmp = br.readLine();
        StringWriter writer = new StringWriter();
        while (tmp != null) {
            writer.write(tmp + "\n");
            tmp = br.readLine();
        }
        writer.flush();
        writer.close();
        String s = writer.toString();
        String pattern = "<input type=\"hidden\" name=\"id\" value=\"";
        int i = s.indexOf(pattern);
        String id = s.substring(i + pattern.length(), s.indexOf("\"", i + pattern.length() + 1));
        System.out.println(id);
        URL dl = new URL("https://" + args[0] + "/download/apply_downloadtaskadd.cgi");
        ClientHttpRequest cr = new ClientHttpRequest(dl.openConnection());
        http = (HttpURLConnection) cr.getConnection();
        ssl = (HttpsURLConnectionImpl) http;
        ssl.setSSLSocketFactory(factory);
        ssl.setHostnameVerifier(new HostnameVerifier() {

            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("upload_type", "url");
        map.put("file", "");
        map.put("url", "http://www.mac4ever.com/images/images_actu/34340_584_des_precisions_sur_le_macbook_air.jpg");
        InputStream post = cr.post(map);
        br = new BufferedReader(new InputStreamReader(post));
        tmp = br.readLine();
        writer = new StringWriter();
        while (tmp != null) {
            writer.write(tmp + "\n");
            tmp = br.readLine();
        }
        writer.flush();
        writer.close();
        System.out.println("alors ?" + writer);
    }
