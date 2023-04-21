    public InetAddress testOutbound(InetAddress bind_ip, int bind_port) throws NetworkAdminException {
        if (bind_ip != null || bind_port != 0) {
            throw (new NetworkAdminException("HTTP tester doesn't support local bind options"));
        }
        try {
            return (VersionCheckClient.getSingleton().getExternalIpAddressHTTP(false));
        } catch (Throwable e) {
            try {
                URL url = new URL("http://www.google.com/");
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(10000);
                connection.connect();
                return (null);
            } catch (Throwable f) {
                throw (new NetworkAdminException("Outbound test failed", e));
            }
        }
    }
