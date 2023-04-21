    public void connect() throws UnknownHostException, IOException, LoginFailedException, IncorrectPasswordException {
        socket = new Socket(host, port);
        socket.setTcpNoDelay(true);
        socket.setKeepAlive(true);
        inputStream = new PacketInputStream(socket.getInputStream());
        outputStream = new PacketOutputStream(socket.getOutputStream());
        writeQueue = new LinkedBlockingQueue<Packet>();
        handlerQueue = new LinkedBlockingQueue<Packet>();
        requestDispatcher = new RequestDispatcher(writeQueue);
        reader = new Thread(new Runnable() {

            public void run() {
                try {
                    while (!Thread.interrupted()) handlePacket(inputStream.readPacket());
                } catch (IOException e) {
                    if (!Thread.interrupted()) close(e);
                } catch (Exception e) {
                    close(e);
                }
            }
        }, "Packet Reader Thread");
        writer = new Thread(new Runnable() {

            public void run() {
                try {
                    while (true) {
                        Packet p = writeQueue.take();
                        outputStream.writePacket(p);
                    }
                } catch (InterruptedException e) {
                } catch (IOException e) {
                    close(e);
                } catch (Exception e) {
                    close(e);
                }
            }
        }, "Packet Writer Thread");
        reader.start();
        writer.start();
        try {
            connected = true;
            doLogin(password);
        } catch (LoginFailedException ex) {
            close(ex);
            throw ex;
        } catch (IncorrectPasswordException ex) {
            close(ex);
            throw ex;
        } finally {
            connected = false;
        }
        setConnected(true);
    }
