    private URLConnection getConnection(String strUrl) {
        URL url = null;
        IConfigurationSv cfg = DefaultSvFactory.getInstance().getConfigurationSv();
        URLConnection connection;
        String configProxyIp = cfg.getValueString(ConfigCo.COMMON_PROXY_IP);
        int configProxyPort = 0;
        try {
            configProxyPort = cfg.getValueInt(ConfigCo.COMMON_PROXY_PORT);
        } catch (NoNumberFormatException e) {
            e.printStackTrace();
        }
        try {
            if (!strUrl.startsWith("http")) {
                strUrl = "http://" + strUrl;
            }
            url = new URL(strUrl);
            if (StringUtils.isBlank(configProxyIp)) {
                connection = url.openConnection();
            } else {
                SocketAddress address = new InetSocketAddress(configProxyIp, configProxyPort);
                Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
                connection = url.openConnection(proxy);
            }
        } catch (MalformedURLException e) {
            throw new IcehorsetoolsRuntimeException(MessageFormat.format(Lang.get(this.getClass(), "MalformedURLException"), new Object[] { url.toString() }));
        } catch (IOException e) {
            throw new IcehorsetoolsRuntimeException(MessageFormat.format(Lang.get(this.getClass(), "IOException"), new Object[] { url.toString() }));
        }
        return connection;
    }
