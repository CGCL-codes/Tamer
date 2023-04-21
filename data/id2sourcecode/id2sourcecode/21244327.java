        public void run() {
            try {
                Main.debug("begin ssl...");
                connection = (SecureConnection) Connector.open("ssl://talk.google.com:5223");
                connection.setSocketOption(SocketConnection.LINGER, 5);
                Main.debug("socket connected.");
                reader = new InputStreamReader(connection.openInputStream(), "UTF-8");
                writer = new OutputStreamWriter(connection.openOutputStream(), "UTF-8");
                writer.write(XML_START);
                writer.flush();
                while (true) {
                    String token = ReceivingThread.blockReadToken(reader);
                    Main.debug(token);
                    if (ReceivingThread.isStartToken(token, "stream:stream")) {
                        service.sessionId = ReceivingThread.getAttribute(token, "id");
                    }
                    if (ReceivingThread.isEndToken(token, "stream:features")) {
                        SendingThread.blockWriteToken(writer, getLoginXml());
                        String result = ReceivingThread.blockReadToken(reader);
                        if (ReceivingThread.isStartToken(result, "iq") && !"error".equals(ReceivingThread.getAttribute(result, "type"))) {
                            Main.debug("Login ok!");
                            break;
                        } else {
                            connection.close();
                            connection = null;
                            listener.connectionError("Failed login.");
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                Main.debug(e.getMessage());
                e.printStackTrace();
                connection = null;
                listener.connectionError(e.getMessage());
                return;
            }
            sendingThread = new SendingThread(writer, service);
            receivingThread = new ReceivingThread(reader, service);
            sendingThread.start();
            receivingThread.start();
            listener.connectionEstablished();
            connectingThread = null;
            Main.debug("connecting thread ended normally.");
        }
