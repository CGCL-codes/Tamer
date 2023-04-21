    protected URL getURL(String serviceName, boolean authenticated) throws MalformedURLException {
        String url = TestProps.get("soap.url");
        if (authenticated) {
            String userId = TestProps.get("soap.user.id");
            String password = Digester.digest(TestProps.get("soap.password"));
            int pos = url.indexOf("://");
            String protocol = url.substring(0, pos + 3);
            String host = url.substring(pos + 3, url.length());
            url = protocol + userId + ":" + password + "@" + host + "/tunnel-web/secure/axis/" + serviceName;
        } else {
            url += "/tunnel-web/axis/" + serviceName;
        }
        return new URL(url);
    }
