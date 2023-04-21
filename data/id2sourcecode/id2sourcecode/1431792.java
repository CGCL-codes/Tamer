    public InputStream getInputStream(URL url) throws IOException {
        InputStream stream = null;
        if (this.useProxy) {
            SocketAddress addr = new InetSocketAddress(proxyHost, proxyPort);
            java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, addr);
            stream = url.openConnection(proxy).getInputStream();
        } else {
            stream = url.openConnection(java.net.Proxy.NO_PROXY).getInputStream();
        }
        return stream;
    }
