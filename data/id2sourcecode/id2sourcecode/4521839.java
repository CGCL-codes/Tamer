    public ExternalClient(ExternalCommunicationServer flashCommServer, int id, Socket clientSock, BufferedReader readerIn, PrintStream writerOut) {
        server = flashCommServer;
        clientID = id;
        clientSocket = clientSock;
        toClient = writerOut;
        fromClient = readerIn;
        clientThread = new Thread(new Runnable() {

            public void run() {
                DebugUtils.println("External Client Connected: ID == " + clientID);
                while (!done) {
                    String command = null;
                    try {
                        command = fromClient.readLine();
                        if (command == null) {
                            done = true;
                            clientSocket.close();
                        } else {
                            command = command.trim();
                            server.handleCommand(ExternalClient.this, command);
                        }
                    } catch (IOException e) {
                        String msg = e.getMessage();
                        if (msg.contains("Connection reset") || msg.contains("socket closed")) {
                            DebugUtils.println("Client Logged Off: " + clientID);
                            done = true;
                        } else {
                            DebugUtils.println(msg);
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        clientThread.start();
    }
