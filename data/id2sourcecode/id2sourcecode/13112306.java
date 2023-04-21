    @Override
    public void run() {
        while (inLoop) {
            String readGet = readGetHTTP();
            if (readGet.isEmpty()) {
                inLoop = false;
                continue;
            }
            if (remoteLocalThread != null) {
                try {
                    remoteLocalThread.setMustEnd();
                    remoteLocalThread.interrupt();
                } catch (Exception e) {
                }
            }
            HostPort host = null;
            try {
                host = Utils.parseHost(readGet);
                if (host == null) {
                    continue;
                }
                setName(host.getHTTPString());
                if (Utils.isLocal(host.getHost())) {
                    URL url = Utils.parseURL(readGet);
                    readGet = Utils.replaceHost(readGet, url, host);
                    connect = host;
                } else {
                    connect = Runner.url;
                }
            } catch (NullPointerException e) {
                connect = Runner.url;
            }
            try {
                if (lastConnect != connect) {
                    endSock();
                    connection = new Socket(connect.getHost(), connect.getPort());
                    lastConnect = connect;
                    System.out.println("Connected to '" + host.getHTTPString() + "' via '" + connection.getInetAddress().getHostName() + ":" + connection.getPort() + "'");
                } else {
                    System.out.println("Reused connection: '" + host.getHTTPString() + "'");
                }
                System.out.flush();
                outputStream2 = connection.getOutputStream();
                inputStream2 = connection.getInputStream();
                remoteLocalThread = new RemoteInputThread(inputStream2, outputStream, this);
                remoteLocalThread.start();
                int size = Utils.getContentLength(readGet);
                writeOutput(outputStream2, readGet);
                if (size > 0) {
                    int b;
                    byte[] buffer = new byte[RemoteInputThread.BUFFER_SIZE];
                    while (size > 0) {
                        if (size < buffer.length) {
                            b = inputStream.read(buffer, 0, size);
                        } else {
                            b = inputStream.read(buffer);
                        }
                        if (b == -1) {
                            break;
                        }
                        outputStream2.write(buffer, 0, b);
                        outputStream2.flush();
                        size -= b;
                    }
                }
            } catch (ConnectException e) {
                System.out.println("Connection closed (" + host.getHTTPString() + ")");
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    this.finalize();
                } catch (Throwable ex) {
                }
            }
        }
        endThread();
    }
