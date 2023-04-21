    @Override
    public void run() {
        try {
            StringBuilder receivedText = new StringBuilder(Consts.XmlClientBufferSize * 5);
            ByteBuffer receiveBuffer = ByteBuffer.allocateDirect(Consts.XmlClientBufferSize);
            while (this.started) {
                receiveBuffer.clear();
                int receivedBytesCount = this.socket.read(receiveBuffer);
                if (receivedBytesCount == -1) {
                    this.log.write(Level.WARNING, String.format(this.messages.getString("XmlClientClosedSocket"), this.remoteAddress), this.genericClient);
                    break;
                }
                receiveBuffer.flip();
                receivedText.append(this.decoder.decode(receiveBuffer).toString());
                this.log.write(Level.INFO, String.format(this.messages.getString("XmlClientReceivedBytes"), receivedBytesCount, this.remoteAddress), this.genericClient);
                String xmlEnd = "</" + Consts.ElementNameUIProtocol + ">";
                int indexEnd = receivedText.indexOf(xmlEnd + Consts.NullByteChar);
                int indexStart = 0;
                if (indexEnd != -1) {
                    indexEnd += xmlEnd.length();
                    indexStart = indexEnd + 1;
                }
                if (indexEnd == -1) {
                    indexEnd = receivedText.indexOf(String.valueOf(Consts.NullByteChar));
                    if (indexEnd != -1) {
                        indexStart = indexEnd + 1;
                    }
                    if (indexEnd == -1) {
                        indexEnd = receivedText.indexOf(xmlEnd);
                        if (indexEnd != -1) {
                            indexEnd += xmlEnd.length();
                            indexStart = indexEnd;
                        }
                    }
                }
                while (indexEnd != -1) {
                    String textToDecode = receivedText.substring(0, indexEnd);
                    receivedText = new StringBuilder(receivedText.substring(indexStart, receivedText.length()));
                    if (!textToDecode.equals("")) {
                        this.log.write(Level.FINE, String.format(this.messages.getString("XmlReceived"), textToDecode), this.genericClient);
                        try {
                            UIProtocolOut uipOut = this.uipXmlTreeFactory.parseXml(textToDecode);
                            if (this.started) {
                                this.genericClient.handleMessage(uipOut);
                            }
                        } catch (JAXBException ex) {
                            this.log.write(Level.SEVERE, String.format("%s,%s", Consts.ErrorXmlSchemaValidation, ex.toString()), this.genericClient);
                            this.genericClient.sendErrorResponse(Consts.ErrorXmlSchemaValidation, Consts.PropertyValueError, ex.toString());
                        }
                    }
                    indexEnd = receivedText.indexOf(xmlEnd + Consts.NullByteChar);
                    indexStart = 0;
                    if (indexEnd != -1) {
                        indexEnd += xmlEnd.length();
                        indexStart = indexEnd + 1;
                    }
                    if (indexEnd == -1) {
                        indexEnd = receivedText.indexOf(String.valueOf(Consts.NullByteChar));
                        if (indexEnd != -1) {
                            indexStart = indexEnd + 1;
                        }
                        if (indexEnd == -1) {
                            indexEnd = receivedText.indexOf(xmlEnd);
                            if (indexEnd != -1) {
                                indexEnd += xmlEnd.length();
                                indexStart = indexEnd;
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
        } catch (AsynchronousCloseException ex) {
            this.log.write(Level.WARNING, this.messages.getString("XmlClientViolentlyDisconected"), this.genericClient);
        } catch (IOException ex) {
            this.log.write(Level.SEVERE, String.format(this.messages.getString("XmlClientSocketException"), ex.toString()), this.genericClient);
            this.genericClient.clientDisconnected();
        } finally {
            this.started = false;
            this.genericClient.removeClient();
            try {
                this.socket.socket().close();
                this.socket.close();
                this.log.write(Level.INFO, this.messages.getString("ThreadStopped"), this.genericClient);
            } catch (Exception ex) {
            }
            this.genericClient.communicationClosed();
        }
    }
