    protected Socket accept() throws IOException {
        if (nio_servsock == null) return null;
        Socket sock;
        try {
            if (DEBUG) System.err.println("LSS: Calling nio_servsock.accept");
            SocketChannel sc = nio_servsock.accept();
            if (sc == null) return null;
            sock = sc.socket();
            if (DEBUG) System.err.println("LSS: nio_servsock.accept returned " + sock);
            sock.getChannel().configureBlocking(false);
        } catch (SocketException e) {
            if (DEBUG) System.err.println("LSS: nio_servsock.accept got SocketException " + e);
            return null;
        } catch (IOException e) {
            System.err.println("LSS: accept got IOException: " + e);
            e.printStackTrace();
            ATcpServerSocketClosedEvent dead = new ATcpServerSocketClosedEvent(servsock);
            compQ.enqueueLossy(dead);
            listen_nio_selsource.deregister(selkey);
            throw e;
        }
        return sock;
    }
