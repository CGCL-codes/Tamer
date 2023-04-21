    public void run() {
        try {
            if (_sslSlaves) {
                _serverSocket = SSLGetContext.getSSLContext().getServerSocketFactory().createServerSocket(_port);
            } else {
                _serverSocket = new ServerSocket(_port);
            }
            logger.info("Listening for slaves on port " + _port);
        } catch (Exception e) {
            throw new FatalException(e);
        }
        Socket socket = null;
        while (true) {
            RemoteSlave rslave = null;
            ObjectInputStream in = null;
            ObjectOutputStream out = null;
            try {
                socket = _serverSocket.accept();
                socket.setSoTimeout(socketTimeout);
                if (socket instanceof SSLSocket) {
                    ((SSLSocket) socket).setUseClientMode(false);
                    ((SSLSocket) socket).startHandshake();
                }
                logger.debug("Slave connected from " + socket.getRemoteSocketAddress());
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
                String slavename = RemoteSlave.getSlaveNameFromObjectInput(in);
                try {
                    rslave = getRemoteSlave(slavename);
                } catch (ObjectNotFoundException e) {
                    out.writeObject(new AsyncCommandArgument("error", "error", slavename + " does not exist, use \"site addslave\""));
                    logger.info("Slave " + slavename + " does not exist, use \"site addslave\"");
                    socket.close();
                    continue;
                }
                if (rslave.isOnline()) {
                    out.writeObject(new AsyncCommandArgument("", "error", "Already online"));
                    out.flush();
                    socket.close();
                    throw new IOException("Already online");
                }
            } catch (Exception e) {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e1) {
                    }
                }
                logger.error("", e);
                continue;
            }
            try {
                if (!rslave.checkConnect(socket)) {
                    out.writeObject(new AsyncCommandArgument("", "error", socket.getInetAddress() + " is not a valid mask for " + rslave.getName()));
                    logger.error(socket.getInetAddress() + " is not a valid ip for " + rslave.getName());
                    socket.close();
                    continue;
                }
                rslave.connect(socket, in, out);
            } catch (Exception e) {
                rslave.setOffline(e);
                logger.error(e);
            } catch (Throwable t) {
                logger.error("FATAL: Throwable in SalveManager loop");
            }
        }
    }
