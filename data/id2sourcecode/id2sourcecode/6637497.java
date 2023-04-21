    public void accept(MsgContext ep) throws IOException {
        if (sSocket == null) return;
        synchronized (this) {
            while (paused) {
                try {
                    wait();
                } catch (InterruptedException ie) {
                }
            }
        }
        SocketChannel sc = sSocket.getChannel().accept();
        Socket s = sc.socket();
        ep.setNote(socketNote, s);
        if (log.isDebugEnabled()) log.debug("Accepted socket " + s + " channel " + sc.isBlocking());
        try {
            setSocketOptions(s);
        } catch (SocketException sex) {
            log.debug("Error initializing Socket Options", sex);
        }
        requestCount++;
        sc.configureBlocking(false);
        InputStream is = new SocketInputStream(sc);
        OutputStream os = new SocketOutputStream(sc);
        ep.setNote(isNote, is);
        ep.setNote(osNote, os);
        ep.setControl(tp);
    }
