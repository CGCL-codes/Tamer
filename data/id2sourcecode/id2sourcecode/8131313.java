    public static void main(String[] args) throws Exception {
        TwitterChannel chan = new TwitterChannel();
        chan.init();
        chan.setTokenSecret("");
        StringBuilder sign = new StringBuilder("GET&").append(URLEncoder.encode(REQ_URL, "UTF8"));
        sign.append("&oauth_consumer_key%3D").append("Your1AdO6GZUxDGE8sMQdw");
        long now = System.currentTimeMillis();
        sign.append("%26oauth_nonce%3D").append(now);
        sign.append("%26oauth_signature_method%3DHMAC-SHA1");
        sign.append("%26oauth_timestamp%3D").append(now / 1000l);
        String firma = chan.sign(sign.toString());
        StringBuilder auth_header = new StringBuilder("OAuth oauth_consumer_key=\"");
        auth_header.append("Your1AdO6GZUxDGE8sMQdw").append("\",oauth_nonce=\"");
        auth_header.append(now).append("\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"");
        auth_header.append(now / 1000l).append("\",oauth_signature=\"");
        auth_header.append(URLEncoder.encode(firma, "UTF8")).append('"');
        System.out.println("Connecting to Twitter for Request Token...");
        URL url = new URL(REQ_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(20000);
        conn.setReadTimeout(20000);
        conn.setDefaultUseCaches(false);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", auth_header.toString());
        conn.connect();
        byte[] buf = new byte[conn.getContentLength()];
        InputStream ins = conn.getInputStream();
        ins.read(buf);
        conn.disconnect();
        Map<String, String> datos = parseResponse(new String(buf));
        String secreto = datos.get("oauth_token_secret");
        if (secreto == null) {
            System.out.println("Por alguna razon no obtuvimos la respuesta esperada.");
            System.out.printf("Leimos datos: %s%n", datos);
            System.exit(1);
            return;
        }
        String token1 = datos.get("oauth_token");
        System.out.println();
        System.out.println("Please copy this URL and paste it into your browser.");
        System.out.println("You may need to log into twitter.com if you do not have an active session");
        System.out.println("in the browser you use.");
        System.out.println();
        System.out.printf(AUTH_URL, token1);
        System.out.println();
        System.out.println();
        System.out.println("You will get a PIN number; copy it and paste it here: ");
        String pin = null;
        int tries = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while ((pin == null || pin.length() < 7) && tries < 3) {
            if (tries > 0) {
                System.out.println("The PIN code does not seem to be valid, please try again.");
            }
            System.out.print("Enter PIN code: ");
            System.out.flush();
            pin = reader.readLine();
            tries++;
        }
        if (pin == null) {
            System.exit(2);
            return;
        }
        sign = new StringBuilder("GET&").append(URLEncoder.encode(AXS_URL, "UTF8"));
        sign.append("&oauth_consumer_key%3D").append("Your1AdO6GZUxDGE8sMQdw");
        now = System.currentTimeMillis();
        sign.append("%26oauth_nonce%3D").append(now);
        sign.append("%26oauth_signature_method%3DHMAC-SHA1");
        sign.append("%26oauth_timestamp%3D").append(now / 1000l);
        sign.append("%26oauth_token%3D").append(URLEncoder.encode(token1, "UTF8"));
        sign.append("%26oauth_verifier%3D").append(pin);
        chan.setTokenSecret(secreto);
        firma = chan.sign(sign.toString());
        auth_header = new StringBuilder("OAuth oauth_consumer_key=\"Your1AdO6GZUxDGE8sMQdw");
        auth_header.append("\",oauth_nonce=\"").append(now);
        auth_header.append("\",oauth_verifier=\"").append(pin);
        auth_header.append("\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"");
        auth_header.append(now / 1000l).append("\",oauth_token=\"");
        auth_header.append(URLEncoder.encode(token1, "UTF8")).append("\",oauth_signature=\"");
        auth_header.append(URLEncoder.encode(firma, "UTF8")).append('"');
        System.out.println();
        url = new URL(AXS_URL);
        conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(20000);
        conn.setReadTimeout(20000);
        conn.setDefaultUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", auth_header.toString());
        conn.connect();
        buf = new byte[conn.getContentLength()];
        ins = conn.getInputStream();
        ins.read(buf);
        conn.disconnect();
        datos = parseResponse(new String(buf));
        System.out.println();
        System.out.println("ALL DONE!");
        System.out.println("Now you have to use these values as properties in your TwitterChannel component:");
        System.out.printf("AccessToken: %s%n", datos.get("oauth_token"));
        System.out.printf("TokenSecret: %s%n", datos.get("oauth_token_secret"));
        System.out.println();
        System.out.println("In case you're using Spring, here's the XML you need to paste into your application context (inside the bean definition of the TwitterChannel):");
        System.out.printf("    <property name=\"accessToken\" value=\"%s\" />%n", datos.get("oauth_token"));
        System.out.printf("    <property name=\"tokenSecret\" value=\"%s\" />%n", datos.get("oauth_token_secret"));
        System.out.println();
        System.out.printf("If you lose these values then you must run this app again.");
        System.exit(0);
    }
