    private Map<String, String> retrieveLoginPage() throws IOException {
        if (proxyHost.length() > 0) {
            System.setProperty("http.proxyHost", proxyHost);
            System.setProperty("http.proxyPort", String.valueOf(proxyPort));
        }
        URL url = new URL("http://login.yahoo.com/config/login?.src=pg");
        URLConnection uc = url.openConnection();
        Object content = uc.getContent();
        Map<String, String> dict = makeLoginPageDict((String) content);
        return dict;
    }
