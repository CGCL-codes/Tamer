        private void run0() {
            TCPEndpoint endpoint = getEndpoint();
            int port = endpoint.getPort();
            threadConnectionHandler.set(this);
            try {
                socket.setTcpNoDelay(true);
            } catch (Exception e) {
            }
            try {
                if (connectionReadTimeout > 0) socket.setSoTimeout(connectionReadTimeout);
            } catch (Exception e) {
            }
            try {
                InputStream sockIn = socket.getInputStream();
                InputStream bufIn = sockIn.markSupported() ? sockIn : new BufferedInputStream(sockIn);
                bufIn.mark(4);
                DataInputStream in = new DataInputStream(bufIn);
                int magic = in.readInt();
                if (magic == POST) {
                    tcpLog.log(Log.BRIEF, "decoding HTTP-wrapped call");
                    bufIn.reset();
                    try {
                        socket = new HttpReceiveSocket(socket, bufIn, null);
                        remoteHost = "0.0.0.0";
                        sockIn = socket.getInputStream();
                        bufIn = new BufferedInputStream(sockIn);
                        in = new DataInputStream(bufIn);
                        magic = in.readInt();
                    } catch (IOException e) {
                        throw new RemoteException("Error HTTP-unwrapping call", e);
                    }
                }
                short version = in.readShort();
                if (magic != TransportConstants.Magic || version != TransportConstants.Version) {
                    closeSocket(socket);
                    return;
                }
                OutputStream sockOut = socket.getOutputStream();
                BufferedOutputStream bufOut = new BufferedOutputStream(sockOut);
                DataOutputStream out = new DataOutputStream(bufOut);
                int remotePort = socket.getPort();
                if (tcpLog.isLoggable(Log.BRIEF)) {
                    tcpLog.log(Log.BRIEF, "accepted socket from [" + remoteHost + ":" + remotePort + "]");
                }
                TCPEndpoint ep;
                TCPChannel ch;
                TCPConnection conn;
                byte protocol = in.readByte();
                switch(protocol) {
                    case TransportConstants.SingleOpProtocol:
                        ep = new TCPEndpoint(remoteHost, socket.getLocalPort(), endpoint.getClientSocketFactory(), endpoint.getServerSocketFactory());
                        ch = new TCPChannel(TCPTransport.this, ep);
                        conn = new TCPConnection(ch, socket, bufIn, bufOut);
                        handleMessages(conn, false);
                        break;
                    case TransportConstants.StreamProtocol:
                        out.writeByte(TransportConstants.ProtocolAck);
                        if (tcpLog.isLoggable(Log.VERBOSE)) {
                            tcpLog.log(Log.VERBOSE, "(port " + port + ") " + "suggesting " + remoteHost + ":" + remotePort);
                        }
                        out.writeUTF(remoteHost);
                        out.writeInt(remotePort);
                        out.flush();
                        String clientHost = in.readUTF();
                        int clientPort = in.readInt();
                        if (tcpLog.isLoggable(Log.VERBOSE)) {
                            tcpLog.log(Log.VERBOSE, "(port " + port + ") client using " + clientHost + ":" + clientPort);
                        }
                        ep = new TCPEndpoint(remoteHost, socket.getLocalPort(), endpoint.getClientSocketFactory(), endpoint.getServerSocketFactory());
                        ch = new TCPChannel(TCPTransport.this, ep);
                        conn = new TCPConnection(ch, socket, bufIn, bufOut);
                        handleMessages(conn, true);
                        break;
                    case TransportConstants.MultiplexProtocol:
                        if (tcpLog.isLoggable(Log.VERBOSE)) {
                            tcpLog.log(Log.VERBOSE, "(port " + port + ") accepting multiplex protocol");
                        }
                        out.writeByte(TransportConstants.ProtocolAck);
                        if (tcpLog.isLoggable(Log.VERBOSE)) {
                            tcpLog.log(Log.VERBOSE, "(port " + port + ") suggesting " + remoteHost + ":" + remotePort);
                        }
                        out.writeUTF(remoteHost);
                        out.writeInt(remotePort);
                        out.flush();
                        ep = new TCPEndpoint(in.readUTF(), in.readInt(), endpoint.getClientSocketFactory(), endpoint.getServerSocketFactory());
                        if (tcpLog.isLoggable(Log.VERBOSE)) {
                            tcpLog.log(Log.VERBOSE, "(port " + port + ") client using " + ep.getHost() + ":" + ep.getPort());
                        }
                        ConnectionMultiplexer multiplexer;
                        synchronized (channelTable) {
                            ch = getChannel(ep);
                            multiplexer = new ConnectionMultiplexer(ch, bufIn, sockOut, false);
                            ch.useMultiplexer(multiplexer);
                        }
                        multiplexer.run();
                        break;
                    default:
                        out.writeByte(TransportConstants.ProtocolNack);
                        out.flush();
                        break;
                }
            } catch (IOException e) {
                tcpLog.log(Log.BRIEF, "terminated with exception:", e);
            } finally {
                closeSocket(socket);
            }
        }
