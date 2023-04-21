        public void run() {
            while (true) {
                synchronized (this.runLock) {
                    while (this.client == null) {
                        try {
                            this.runLock.wait();
                        } catch (InterruptedException ie) {
                        }
                    }
                }
                this.readByteCount = 0L;
                this.writeByteCount = 0L;
                InetAddress inetAddr = this.client.getInetAddress();
                Print.logInfo("Remote client port: " + inetAddr + ":" + this.client.getPort() + "[" + this.client.getLocalPort() + "]");
                long sessionStartTime = DateTime.getCurrentTimeMillis();
                long sessionTimeoutMS = ServerSocketThread.this.getSessionTimeout();
                long sessionTimeoutAt = (sessionTimeoutMS > 0L) ? (sessionStartTime + sessionTimeoutMS) : -1L;
                ClientPacketHandler clientHandler = ServerSocketThread.this.getClientPacketHandler();
                if (clientHandler != null) {
                    if (clientHandler instanceof AbstractClientPacketHandler) {
                        ((AbstractClientPacketHandler) clientHandler).setSessionInfo(this);
                    }
                    clientHandler.sessionStarted(inetAddr, this.client.isTCP(), ServerSocketThread.this.isTextPackets());
                }
                OutputStream output = null;
                Throwable termError = null;
                try {
                    output = this.client.getOutputStream();
                    if (clientHandler != null) {
                        byte initialPacket[] = clientHandler.getInitialPacket();
                        if ((initialPacket != null) && (initialPacket.length > 0)) {
                            if (this.client.isTCP()) {
                                this.writeBytes(output, initialPacket);
                            } else {
                            }
                        }
                    }
                    for (int i = 0; ; i++) {
                        if (sessionTimeoutAt > 0L) {
                            long currentTimeMS = DateTime.getCurrentTimeMillis();
                            if (currentTimeMS >= sessionTimeoutAt) {
                                throw new SSSessionTimeoutException("Session timeout");
                            }
                        }
                        if (this.client.isTCP()) {
                            byte prompt[] = ServerSocketThread.this.getPrompt(i);
                            if ((prompt != null) && (prompt.length > 0)) {
                                this.writeBytes(output, prompt);
                            }
                        }
                        byte line[] = null;
                        if (ServerSocketThread.this.isTextPackets()) {
                            line = this.readLine(this.client, clientHandler);
                        } else {
                            line = this.readPacket(this.client, clientHandler);
                        }
                        if ((line != null) && ServerSocketThread.this.hasListeners()) {
                            try {
                                ServerSocketThread.this.invokeListeners(line);
                            } catch (Throwable t) {
                                break;
                            }
                        }
                        if ((line != null) && (clientHandler != null)) {
                            try {
                                byte response[] = clientHandler.getHandlePacket(line);
                                if ((response != null) && (response.length > 0)) {
                                    if (this.client.isTCP()) {
                                        this.writeBytes(output, response);
                                    } else {
                                        int rPort = 0;
                                        if (rPort <= 0) {
                                            rPort = clientHandler.getResponsePort();
                                        }
                                        if (rPort <= 0) {
                                            rPort = ServerSocketThread.this.getRemotePort();
                                        }
                                        if (rPort <= 0) {
                                            rPort = this.client.getPort();
                                        }
                                        if (rPort > 0) {
                                            int retry = 1;
                                            DatagramSocket dgSocket = new DatagramSocket();
                                            DatagramPacket respPkt = new DatagramPacket(response, response.length, inetAddr, rPort);
                                            for (; retry > 0; retry--) {
                                                Print.logInfo("Sending Datagram [%s:%d]: %s", inetAddr.toString(), rPort, StringTools.toHexString(response));
                                                dgSocket.send(respPkt);
                                            }
                                        } else {
                                            Print.logWarn("Unable to send response Datagram: unknown port");
                                        }
                                    }
                                } else {
                                }
                                if (clientHandler.terminateSession()) {
                                    break;
                                }
                            } catch (Throwable t) {
                                Print.logException("Unexpected exception: ", t);
                                break;
                            }
                        }
                        if (this.client.isUDP()) {
                            int avail = this.client.available();
                            if (avail <= 0) {
                                break;
                            } else {
                                Print.logInfo("UDP: bytes remaining - %d", avail);
                            }
                        }
                    }
                } catch (SSSessionTimeoutException ste) {
                    Print.logError(ste.getMessage());
                    termError = ste;
                } catch (SSReadTimeoutException rte) {
                    Print.logError(rte.getMessage());
                    termError = rte;
                } catch (SSEndOfStreamException eos) {
                    if (this.client.isTCP()) {
                        Print.logError(eos.getMessage());
                        termError = eos;
                    } else {
                    }
                } catch (SocketException se) {
                    Print.logError("Connection closed");
                    termError = se;
                } catch (Throwable t) {
                    Print.logException("?", t);
                    termError = t;
                }
                if (clientHandler != null) {
                    try {
                        byte finalPacket[] = clientHandler.getFinalPacket(termError != null);
                        if ((finalPacket != null) && (finalPacket.length > 0)) {
                            if (this.client.isTCP()) {
                                this.writeBytes(output, finalPacket);
                            } else {
                                int rPort = 0;
                                if (rPort <= 0) {
                                    rPort = clientHandler.getResponsePort();
                                }
                                if (rPort <= 0) {
                                    rPort = ServerSocketThread.this.getRemotePort();
                                }
                                if (rPort <= 0) {
                                    rPort = this.client.getPort();
                                }
                                if (rPort > 0) {
                                    int retry = 1;
                                    DatagramSocket dgSocket = new DatagramSocket();
                                    DatagramPacket respPkt = new DatagramPacket(finalPacket, finalPacket.length, inetAddr, rPort);
                                    for (; retry > 0; retry--) {
                                        Print.logInfo("Sending Datagram [%s:%d]: %s", inetAddr.toString(), rPort, StringTools.toHexString(finalPacket));
                                        dgSocket.send(respPkt);
                                    }
                                } else {
                                    Print.logWarn("Unable to send final packet Datagram: unknown port");
                                }
                            }
                        }
                    } catch (Throwable t) {
                        Print.logException("Final packet transmission", t);
                    }
                    clientHandler.sessionTerminated(termError, this.readByteCount, this.writeByteCount);
                    if (clientHandler instanceof AbstractClientPacketHandler) {
                        ((AbstractClientPacketHandler) clientHandler).setSessionInfo(null);
                    }
                }
                if (output != null) {
                    try {
                        output.flush();
                    } catch (IOException ioe) {
                        Print.logException("Flush", ioe);
                    } catch (Throwable t) {
                        Print.logException("?", t);
                    }
                }
                try {
                    this.client.setSoLinger(ServerSocketThread.this.getLingerTimeoutSec());
                } catch (SocketException se) {
                    Print.logException("setSoLinger", se);
                } catch (Throwable t) {
                    Print.logException("?", t);
                }
                try {
                    this.client.close();
                } catch (IOException ioe) {
                }
                synchronized (this.runLock) {
                    this.client = null;
                }
            }
        }
