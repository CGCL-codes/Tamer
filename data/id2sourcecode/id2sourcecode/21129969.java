    public void run() {
        try {
            Socket socket = (Socket) provideResource(m_event);
            SOAPHTTPServerRequest request = (SOAPHTTPServerRequest) m_event.getConnection().getRequest();
            getLogger(m_event).logMessage(0, "Executing request: " + m_event.getEventType());
            String outString = createRequestHeaders(request) + request.getSOAPRequest();
            getLogger(m_event).logMessage(10, "SOAPHTTPServerAdapter: Sending message: \n" + outString);
            socket.getOutputStream().write(outString.getBytes());
            boolean gotResponse = false;
            InputStream inputStream = null;
            int responseStatus = 500;
            int responseLength = 0;
            Vector responseHeaders = new Vector();
            while (!gotResponse) {
                inputStream = socket.getInputStream();
                responseHeaders = getResponseHeaders(m_event, inputStream);
                responseStatus = getResponseStatus(m_event, responseHeaders);
                responseLength = getResponseLength(m_event, responseHeaders);
                if (responseStatus != 100) gotResponse = true;
            }
            String response = "";
            if (responseLength > 0) {
                getLogger(m_event).logMessage(0, "SOAPHTTP Server Adapter: Response length is: " + responseLength);
                byte[] responseBytes = new byte[READ_BLOCK_SIZE];
                StringBuffer buffer = new StringBuffer();
                int totalBytesRead = 0;
                while (totalBytesRead < responseLength) {
                    int bytesRead = inputStream.read(responseBytes);
                    if (bytesRead == -1) break;
                    buffer.append(new String(responseBytes, 0, bytesRead));
                    responseBytes = new byte[READ_BLOCK_SIZE];
                    totalBytesRead += bytesRead;
                }
                getLogger(m_event).logMessage(0, "SOAPHTTP Server Adapter: Actual bytes read: " + totalBytesRead);
                response = buffer.toString();
            } else if (isChunked(m_event, responseHeaders)) {
                getLogger(m_event).logMessage(10, "SOAPHTTP Server Adapter: Response is chunked...");
                int chunkLength = getChunkLength(m_event, inputStream);
                StringBuffer buffer = new StringBuffer();
                while (chunkLength > 0) {
                    byte[] responseBytes = new byte[READ_BLOCK_SIZE];
                    int totalBytesRead = 0;
                    while (totalBytesRead < chunkLength) {
                        int maxBytes = Math.min(READ_BLOCK_SIZE, chunkLength - totalBytesRead);
                        int bytesRead = inputStream.read(responseBytes, 0, maxBytes);
                        if (bytesRead == -1) break;
                        buffer.append(new String(responseBytes, 0, bytesRead));
                        responseBytes = new byte[READ_BLOCK_SIZE];
                        totalBytesRead += bytesRead;
                    }
                    getLogger(m_event).logMessage(10, "SOAPHTTP Server Adapter: Chunk bytes read: " + totalBytesRead + " of " + chunkLength);
                    chunkLength = getChunkLength(m_event, inputStream);
                }
                response = buffer.toString();
            } else {
                getLogger(m_event).logError(8, "SOAPHTTP Server Adapter: Warning: Content length not specified in HTTP response.");
                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < MAX_HTTP_RESPONSE_BODY_LENGTH; i++) {
                    int c = inputStream.read();
                    if (c == -1) break;
                    buffer.append((char) c);
                }
                response = buffer.toString();
            }
            ((ServerConnection) m_event.getConnection()).setServerResource(this, socket);
            m_event.getConnection().setResponse(response);
            getLogger(m_event).logMessage(0, "SOAPHTTP Server Adapter received: " + response);
        } catch (ServerResourceException e) {
            getLogger(m_event).logError(3, "SOAPHTTP Server Adapter failed to obtain socket.");
            m_event.setStatus(AppError.ms_serverError, "SOAPHTTPServerAdapter failed to connect to server.");
            m_event.getConnection().setError(AppError.ms_serverError);
        } catch (Exception e) {
            getLogger(m_event).logError(3, "SOAPHTTP Server Adapter failed to write or read from socket. " + "Exception: " + e);
            m_event.setStatus(AppError.ms_serverError, "SOAPHTTPServerAdapter failed to connect to server.");
            m_event.getConnection().setError(AppError.ms_serverError);
        }
    }
