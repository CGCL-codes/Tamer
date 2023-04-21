    @Override
    public void run() {
        try {
            StringBuilder receivedText = new StringBuilder(Consts.SignalClientBufferSize * 5);
            byte receiveBuffer[] = new byte[Consts.SignalClientBufferSize];
            while (started && input != null) {
                int receivedBytesCount = input.read(receiveBuffer);
                if (receivedBytesCount == -1) {
                    Log.write(Level.WARNING, "SignalClient", "Receive", String.format(Messages.getString("SignalClientClosedSocket"), remoteAddress));
                    break;
                }
                Log.write(Level.INFO, "SignalClient", "Receive", String.format(Messages.getString("SignalClientReceivedBytes"), receivedBytesCount, remoteAddress));
                receivedText.append(new String(receiveBuffer, 0, receivedBytesCount, "UTF-8"));
                int indexReq = receivedText.indexOf(String.valueOf(Consts.NullByteChar));
                while (indexReq != -1) {
                    indexReq++;
                    String textToDecode = receivedText.substring(0, indexReq - 1);
                    receivedText = new StringBuilder(receivedText.substring(indexReq, receivedText.length()));
                    if (!textToDecode.equals("")) {
                        Log.write(Level.FINE, "SignalClient", "Receive", String.format(Messages.getString("SignalReceived"), textToDecode));
                        try {
                            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                            Document document = dBuilder.parse(new ByteArrayInputStream(textToDecode.getBytes("UTF-8")));
                            parseSignal(document);
                        } catch (ParserConfigurationException ex) {
                            Log.write(Level.SEVERE, "SignalClient", "Receive", ex.toString());
                        } catch (SAXParseException ex) {
                            Log.write(Level.SEVERE, "SignalClient", "Receive", ex.toString());
                        } catch (SAXException ex) {
                            Log.write(Level.SEVERE, "SignalClient", "Receive", String.format("%s,%s", Consts.ErrorXmlSchemaValidation, ex.toString()));
                        }
                    }
                    indexReq = receivedText.indexOf(String.valueOf(Consts.NullByteChar));
                }
            }
        } catch (SSLException ex) {
        } catch (IOException ex) {
            Log.write(Level.SEVERE, "SignalClient", "Receive", String.format(Messages.getString("SignalClientSocketException"), ex.toString()));
        } finally {
            server.getClients().remove(this);
            try {
                socket.close();
                Log.write(Level.INFO, "SignalClient", "Receive", Messages.getString("ThreadStopped"));
            } catch (Exception ex) {
            }
        }
    }
