    private HttpURLConnection getConnection(String url) throws IOException {
        HttpURLConnection connection = null;
        if (config.getProxyHost() != null && config.getProxyHost().length() > 0) {
            if (config.getProxyUsername() != null && config.getProxyUsername().length() > 0) {
                Authenticator.setDefault(new Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        if (getRequestorType().equals(RequestorType.PROXY)) {
                            return new PasswordAuthentication(config.getProxyUsername(), config.getProxyPassword().toCharArray());
                        } else {
                            return null;
                        }
                    }
                });
            }
            Proxy proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(config.getProxyHost(), config.getProxyPort()));
            connection = (HttpURLConnection) new URL(url).openConnection(proxy);
        } else {
            connection = (HttpURLConnection) new URL(url).openConnection();
        }
        if (config.getConnectTimeout() > -1) {
            connection.setConnectTimeout(config.getConnectTimeout());
        }
        if (config.getReadTimeout() > -1) {
            connection.setReadTimeout(config.getReadTimeout());
        }
        return connection;
    }
