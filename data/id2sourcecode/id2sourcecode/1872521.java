    @Override
    public File getFileFromAuthorizedStream(URL url, String filename, String user, String passwd) throws IOException {
        IConfigurationSv cfg = DefaultSvFactory.getInstance().getConfigurationSv();
        String configProxyIp = cfg.getValueString(ConfigCo.COMMON_PROXY_IP);
        int configProxyPort = 0;
        try {
            configProxyPort = cfg.getValueInt(ConfigCo.COMMON_PROXY_PORT);
        } catch (NoNumberFormatException e) {
            e.printStackTrace();
        }
        String s = user + ":" + passwd;
        String base64 = "Basic " + new sun.misc.BASE64Encoder().encode(s.getBytes());
        URLConnection conn;
        if (StringUtils.isBlank(configProxyIp)) {
            conn = url.openConnection();
        } else {
            SocketAddress address = new InetSocketAddress(configProxyIp, configProxyPort);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
            conn = url.openConnection(proxy);
        }
        conn.setRequestProperty("Authorization", base64);
        conn.connect();
        InputStream fis = conn.getInputStream();
        File down = new File(KeEnvironment.getSessionTmpDir() + "/" + filename);
        FileOutputStream fos = new FileOutputStream(down);
        try {
            byte[] buffer = new byte[1024];
            int numRead;
            while ((numRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, numRead);
            }
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
        return down;
    }
