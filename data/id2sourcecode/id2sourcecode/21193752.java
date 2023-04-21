    protected HandleMode tunnel(HTTPInputStream in, MultiOutputStream out, WebConnection wc) throws IOException {
        SocketChannel rcc = in.getSocketChannel();
        SocketChannel wcc = (SocketChannel) out.getChannel();
        if (rcc != null && wcc != null) {
            SocketChannel rwc = (SocketChannel) wc.getOutputStream().getChannel();
            SocketChannel wwc = wc.getInputStream().getSocketChannel();
            if (rwc != null && wwc != null) {
                proxy.getCounter().inc("Tunneles handled by channels");
                channelTunnel(rcc, wcc, rwc, wwc);
                return HandleMode.CHANNELED;
            }
        }
        proxy.getCounter().inc("Tunneles handled by threads");
        OutputStream server = wc.getOutputStream();
        InputStream resp = wc.getInputStream();
        byte[] buf = new byte[1024];
        int read;
        new CopyThread(resp, out);
        while ((read = in.read(buf)) > 0) {
            server.write(buf, 0, read);
            server.flush();
        }
        return HandleMode.THREADED;
    }
