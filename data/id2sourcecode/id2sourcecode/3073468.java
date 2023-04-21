    public void connect() throws Exception {
        String link = connection.line;
        url = new URL(link);
        URLConnection conn = null;
        if (host == null) {
            conn = url.openConnection();
            if (runner != null && runner.verboseConnect) System.out.println("Direct connection to " + url);
        } else {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(InetAddress.getByName(host), port()));
            conn = url.openConnection(proxy);
            if (runner != null && runner.verboseConnect) System.out.println("Proxy connection to " + url + " through " + proxy);
        }
        in = conn.getInputStream();
        byte[] buff = new byte[bufferSize];
        int curLen = 0;
        int readLen = 0;
        while ((readLen = in.read(buff)) > 0 && readFull) {
            curLen += readLen;
        }
        curLen += readLen;
        in.close();
        totLen += curLen;
        if (maxLen < curLen) maxLen = curLen;
        readPages++;
    }
