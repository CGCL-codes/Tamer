    public ListenersCollection(final pnlServerGUI server) {
        Globals.getInstance().getDlgOptions().addListener(new SettingsListenerIF() {

            public void ServerFileListDeleted() {
                server.pnlInput.refreshList();
            }

            public void ClientFileListDeleted() {
            }

            public void ServerSentWindowDataBufferSize(int size) {
                server.pnlSentData.setMaxKBytes(size);
            }

            public void ServerReceivedWindowDataBufferSize(int size) {
                server.pnlReceivedData.setMaxKBytes(size);
            }

            public void ClientSentWindowDataBufferSize(int size) {
            }

            public void ClientReceivedWindowDataBufferSize(int size) {
            }

            public void SendingDataChunkSize(int size) {
                server.pnlInput.setPacketSize(size);
            }

            public void ServerLoggingSettingsChanged(String path, int size, int filesCount, boolean isLoggingEnabled) {
                server.sl = new StreamLogger(path, "server.log", size, filesCount);
                server.sl.setEnableLogging(isLoggingEnabled);
            }

            public void ClientLoggingSettingsChanged(String path, int size, int filesCount, boolean isLoggingEnabled) {
            }
        });
        Globals.getInstance().getServerClientSender().addListener(new ServerClientSenderListenerIF() {

            public void DataSent(final ChannelBuffer bufferedMessage, final String ClientID) {
                java.awt.EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        server.outputDataSentFromServer(bufferedMessage, ClientID);
                    }
                });
            }

            public void DataTransmissionError(String ClientID) {
                server.pnlSystemLog.addLog("Some data tranmission error while data sending to client number: " + ClientID + " occure.", new MessageParameters(MessageParameters.messageType.system, MessageParameters.textStyle.error));
            }
        });
        Globals.getInstance().getServer().addListener(new ServerEventsListenerIF() {

            public void DataArrived(ChannelHandlerContext ctx, final MessageEvent e) {
                if (server.isForwarderConnected() == true) {
                    java.awt.EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            server.outputDataArrivedToServerAndForwardedOut((BigEndianHeapChannelBuffer) e.getMessage(), e.getChannel().getId().toString());
                        }
                    });
                    server.sendDataFromForwarderOut((BigEndianHeapChannelBuffer) e.getMessage());
                } else {
                    java.awt.EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            server.outputDataArrivedToServer((BigEndianHeapChannelBuffer) e.getMessage(), e.getChannel().getId().toString());
                        }
                    });
                }
            }

            public void ChannelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
                server.pnlSystemLog.addLog("A client: " + e.getChannel().getId() + " " + e.getChannel().getRemoteAddress().toString() + " connected.", new MessageParameters(MessageParameters.messageType.system, MessageParameters.textStyle.boldGreen, e.getChannel().getId().toString()));
            }

            public void ChannelClose(ChannelHandlerContext ctx, ChannelStateEvent e) {
                server.pnlSystemLog.addLog("A client: " + e.getChannel().getId() + " " + e.getChannel().getRemoteAddress().toString() + " disconnected.", new MessageParameters(MessageParameters.messageType.system, MessageParameters.textStyle.boldOrange, e.getChannel().getId().toString()));
            }

            public void ExceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
                server.pnlSystemLog.addLog("Error: " + e.getCause().getMessage(), new MessageParameters(MessageParameters.messageType.system, MessageParameters.textStyle.error, e.getChannel().getId().toString()));
            }
        });
        Globals.getInstance().getForwarder().addListener(new ClientEventsListenerIF() {

            public void DataArrived(ChannelHandlerContext ctx, final MessageEvent e) {
                java.awt.EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        server.outputDataArrivedToForwarder((BigEndianHeapChannelBuffer) e.getMessage());
                    }
                });
                server.sendDataFromForwarderToClient((BigEndianHeapChannelBuffer) e.getMessage());
            }

            public void Connected(ChannelHandlerContext ctx, ChannelStateEvent e) {
                server.setForwarderConnected(true);
                server.lockForwarderFields(true);
                server.pnlSystemLog.addLog("Connected to server: 127.0.0.1:" + server.forwarderPort, new MessageParameters(MessageParameters.messageType.system, MessageParameters.textStyle.boldGreen));
                server.forwarderTimer.start();
                server.fireServerForwardingStart();
            }

            public void Disconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
                Globals.getInstance().getForwarder().shutdown();
                server.pnlSystemLog.addLog("Disconnected from server 127.0.0.1:" + server.forwarderPort, new MessageParameters(MessageParameters.messageType.system, MessageParameters.textStyle.boldOrange));
                server.setForwarderConnected(false);
                server.lockForwarderFields(false);
                server.cmdDisconnect.setSelected(true);
                server.forwarderTimer.stop();
                server.fireServerForwardingStop();
            }

            public void ExceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
                server.pnlSystemLog.addLog("Error: " + e.getCause().getMessage(), new MessageParameters(MessageParameters.messageType.system, MessageParameters.textStyle.error, e.getChannel().getId().toString()));
            }

            public void DataSentFromQueue(final ChannelBuffer bufferedMessage) {
                java.awt.EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        server.outputDataSentFromForwarder(bufferedMessage);
                    }
                });
            }
        });
        server.pnlClientsBar1.addEventsListener(new ButtonBarListenerIF() {

            public void OneSecondTimerTicked() {
                server.fireServerUpTimeTicker();
            }

            public void ClientSelected(String clientId) {
                Globals.getInstance().getServerClientSender().setChannel(Server.getAllChannels().find(Integer.parseInt(clientId)));
                server.pnlSystemLog.addLog("A client number: " + clientId + " has been chosen to work with.", new MessageParameters(MessageParameters.messageType.system, MessageParameters.textStyle.boldBlue, clientId));
            }

            public void DisconnectionRequested(String clientId) {
                server.pnlSystemLog.addLog("A disconnection request from client number: " + clientId + " has been accepted.", new MessageParameters(MessageParameters.messageType.system, MessageParameters.textStyle.boldBlue, clientId));
                try {
                    Channel channel = Server.getAllChannels().find(Integer.parseInt(clientId));
                    channel.disconnect();
                } catch (NumberFormatException ex) {
                    server.pnlSystemLog.addLog("Getting channel of client number: " + clientId + " failed.", new MessageParameters(MessageParameters.messageType.system, MessageParameters.textStyle.error));
                }
            }
        });
        server.pnlInput.addEventsListener(new InputPanelListenerIF() {

            public void ExceptionCaught(String msg) {
                server.pnlSystemLog.addLog(msg, new MessageParameters(MessageParameters.messageType.system, MessageParameters.textStyle.error));
            }

            public void SendChannelBuffer(final ChannelBuffer bufferedMessage) {
                if (server.pnlInput.isAddCR()) {
                    bufferedMessage.writeByte((byte) 0xd);
                }
                if (server.pnlInput.isAddLF()) {
                    bufferedMessage.writeByte((byte) 0xa);
                }
                if (pnlInput.getDirection() == pnlInput.Direction.CLIENT) {
                    ServerClientQueue.getInstance().put(bufferedMessage);
                } else if (pnlInput.getDirection() == pnlInput.Direction.FORWARDER) {
                    server.sendDataFromForwarderOutAndLogIt(bufferedMessage);
                }
            }
        });
        server.pnlInput.lockButtons(true);
    }
