    private boolean accept() {
        InputStream in = null;
        OutputStream out = null;
        Socket socket = null;
        Channel channel = null;
        try {
            try {
                socket = this.serverSocket.accept();
            } catch (IOException ex) {
                return false;
            }
            in = socket.getInputStream();
            out = socket.getOutputStream();
            ContextRunner runner = new ContextRunner(channel = new Channel(getChannelName(), in, out, socket), logger);
            if (threadPool != null) {
                threadPool.start(runner);
            } else {
                Thread t = new Util.Thread(runner, "JavaBridgeContextRunner(" + contextName + ")");
                t.start();
            }
        } catch (SecurityException t) {
            if (channel != null) channel.shutdown();
            ContextFactory.destroyAll();
            Util.printStackTrace(t);
            return false;
        } catch (Throwable t) {
            if (channel != null) channel.shutdown();
            Util.printStackTrace(t);
        }
        return true;
    }
