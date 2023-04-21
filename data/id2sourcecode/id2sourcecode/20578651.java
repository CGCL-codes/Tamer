    public void run() {
        try {
            Socket socket = (Socket) provideResource(m_event);
            HTTPServerRequest request = (HTTPServerRequest) m_event.getConnection().getRequest();
            String outString = createRequestHeaders(request);
            getLogger(m_event).logMessage(10, "HTTPServerAdapter: Sending message: \n" + outString);
            OutputStream out = socket.getOutputStream();
            out.write(outString.getBytes());
            out.write(request.getRequestBytes());
            boolean gotResponse = false;
            InputStream inputStream = null;
            int responseStatus = 500;
            int responseLength = 0;
            String responseContentType = "";
            String responseLocation = "";
            Vector responseHeaders = new Vector();
            while (!gotResponse) {
                inputStream = socket.getInputStream();
                responseHeaders = getResponseHeaders(inputStream);
                responseStatus = getResponseStatus(responseHeaders);
                responseLength = getResponseLength(responseHeaders);
                responseContentType = getResponseType(responseHeaders);
                if (responseStatus != 100) gotResponse = true;
            }
            if (responseStatus == 302) {
                responseLocation = getResponseLocation(responseHeaders);
                getLogger(m_event).logMessage(8, "HTTPServerAdapter: Re-directing request to: " + responseLocation);
                URL redirectURL = new URL(responseLocation);
                request.setURL(redirectURL);
                handleRequest(m_event);
                return;
            }
            byte[] responseBytes = new byte[1];
            if (responseLength > -1) {
                getLogger(m_event).logMessage(0, "HTTP Server Adapter: Status: " + responseStatus + " Reported Length: " + responseLength);
                responseBytes = new byte[responseLength];
                int totalBytesRead = 0;
                while (totalBytesRead < responseLength) {
                    int bytesRead = inputStream.read(responseBytes, totalBytesRead, responseLength - totalBytesRead);
                    if (bytesRead == -1) break;
                    totalBytesRead += bytesRead;
                }
            } else {
                byte[] buffer = new byte[RESPONSE_BUFFER_LENGTH];
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                boolean moreData = true;
                while (moreData) {
                    int bytesRead = inputStream.read(buffer, 0, RESPONSE_BUFFER_LENGTH);
                    if (bytesRead == -1) {
                        moreData = false;
                    } else if (bytesRead > 0) {
                        byteStream.write(buffer, 0, bytesRead);
                    }
                }
                responseBytes = byteStream.toByteArray();
                getLogger(m_event).logMessage(8, "HTTP Server Adapter: Content length not specified in HTTP response. Bytes read: " + responseBytes.length);
            }
            ((ServerConnection) m_event.getConnection()).setServerResource(this, socket);
            m_event.getConnection().setResponse(new HTTPServerResponse(responseBytes, responseContentType));
        } catch (ServerResourceException e) {
            getLogger(m_event).logError(3, "HTTP Server Adapter failed to obtain socket.");
            m_event.getConnection().setError(AppError.ms_serverError);
        } catch (Exception e) {
            getLogger(m_event).logError(3, "HTTP Server Adapter failed to write or read from socket. " + "Exception: " + e);
            m_event.getConnection().setError(AppError.ms_serverError);
        }
    }
