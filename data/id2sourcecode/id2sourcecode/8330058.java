    public void getAuthenticatedHttpURL() throws Exception {
        String url = null;
        url = "https://eme.mail.accenture.com";
        String username = "carlos.pereira";
        String password = "mypassword";
        InetSocketAddress proxyAddress = new InetSocketAddress(InetAddress.getByName("excelerator.inet"), 3128);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddress);
        URLConnection uc = new URL(url).openConnection(proxy);
        uc.setUseCaches(false);
        if (username != null) {
            Authenticator.setDefault(new BasicAuthenticator(username, password));
        }
        uc.setRequestProperty("pragma", "no-cache");
        uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; (R1 1.5); .NET CLR 1.0.3705; .NET CLR 1.1.4322)");
        println("HEADERS:");
        showObjectProperty(uc, "getContent");
        showObjectProperty(uc, "getContentEncoding");
        showObjectProperty(uc, "getContentLength");
        showObjectProperty(uc, "getContentType");
        showObjectProperty(uc, "getDate", FORMAT.TIMESTAMP);
        showObjectProperty(uc, "getExpiration", FORMAT.TIMESTAMP);
        showObjectProperty(uc, "getLastModified", FORMAT.TIMESTAMP);
    }
