    protected URL getURL(String serviceName, boolean authenticated) throws MalformedURLException {
        String url = PortletProps.get("soap.url");
        if (authenticated) {
            String userId = PortletProps.get("soap.user.id");
            String password = Encryptor.digest(PortletProps.get("soap.password"));
            int pos = url.indexOf("://");
            String protocol = url.substring(0, pos + 3);
            String host = url.substring(pos + 3, url.length());
            url = protocol + userId + ":" + password + "@" + host + "/tunnel-web/secure/axis/" + serviceName;
        } else {
            url += "/tunnel-web/axis/" + serviceName;
        }
        return new URL(url);
    }
