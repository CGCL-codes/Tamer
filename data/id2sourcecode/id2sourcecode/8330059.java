    public void getHttpsURL() throws Exception {
        String url = null;
        url = "https://eme.mail.accenture.com/exchange/carlos.pereira";
        String username = "carlos.pereira";
        String password = "mypassword";
        Authenticator.setDefault(new BasicAuthenticator(username, password));
        InetSocketAddress proxyAddress = new InetSocketAddress(InetAddress.getByName("excelerator.inet"), 3128);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddress);
        HttpURLConnection uc = (HttpURLConnection) new URL(url).openConnection(proxy);
        uc.setUseCaches(false);
        uc.setDoInput(true);
        uc.setDoOutput(true);
        uc.setRequestMethod("GET");
        uc.setRequestProperty("pragma", "no-cache");
        uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; (R1 1.5); .NET CLR 1.0.3705; .NET CLR 1.1.4322)");
        println("HEADERS:");
        Iterator<Map.Entry<String, List<String>>> itProps = uc.getRequestProperties().entrySet().iterator();
        while (itProps.hasNext()) {
            Map.Entry<String, List<String>> e = itProps.next();
            Iterator<?> itValues = e.getValue().iterator();
            while (itValues.hasNext()) {
                println(e.getKey() + ": " + itValues.next());
            }
        }
        uc.connect();
        println(uc.getHeaderField("WWW-Authenticate"));
        println(uc.getResponseMessage());
    }
