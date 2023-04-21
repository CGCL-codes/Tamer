    @Override
    public void run() {
        try {
            StringBuilder receivedText = new StringBuilder(Consts.HttpClientBufferSize * 5);
            ByteBuffer receiveBuffer = ByteBuffer.allocateDirect(Consts.HttpClientBufferSize);
            while (started) {
                receiveBuffer.clear();
                int receivedBytesCount = socket.read(receiveBuffer);
                if (receivedBytesCount == -1 || receivedBytesCount == 0) {
                    break;
                }
                receiveBuffer.flip();
                receivedText.append(decoder.decode(receiveBuffer).toString());
                Log.write(Level.INFO, "HttpClient", "Receive", String.format(Messages.getString("HttpClientRequest"), ((server.getInstanceId() == null) ? "" : server.getInstanceId()), receivedBytesCount, remoteAddress));
                int indexReq = receivedText.indexOf(Consts.HeaderCRLF + Consts.HeaderCRLF);
                while (indexReq != -1) {
                    indexReq += (Consts.HeaderCRLF + Consts.HeaderCRLF).length();
                    String textToDecode = receivedText.substring(0, indexReq);
                    receivedText = new StringBuilder(receivedText.substring(indexReq, receivedText.length()));
                    if (!textToDecode.equals("")) {
                        Log.write(Level.FINE, String.format(Messages.getString("HttpReceived"), ((server.getInstanceId() == null) ? "" : server.getInstanceId()), textToDecode));
                        parseHttpRequest(textToDecode);
                    }
                    indexReq = receivedText.indexOf(Consts.HeaderCRLF + Consts.HeaderCRLF);
                }
            }
        } catch (AsynchronousCloseException ex) {
            Log.write(Level.WARNING, "HttpClient", "Receive", Messages.getString("HttpClientViolentlyDisconected"));
        } catch (IOException ex) {
            Log.write(Level.SEVERE, "HttpClient", "Receive", String.format(Messages.getString("HttpClientSocketException"), ex.toString(), ((server.getInstanceId() == null) ? "" : server.getInstanceId())));
        } finally {
            synchronized (server.getClients()) {
                server.getClients().remove(this);
            }
            try {
                socket.socket().close();
                socket.close();
                Log.write(Level.INFO, String.format(Messages.getString("HttpClientClosedSocket"), remoteAddress, ((server.getInstanceId() == null) ? "" : server.getInstanceId())));
                Log.write(Level.INFO, "HttpClient", ((server.getInstanceId() == null) ? "" : server.getInstanceId()), Messages.getString("ThreadStopped"));
            } catch (Exception ex) {
            }
        }
    }
