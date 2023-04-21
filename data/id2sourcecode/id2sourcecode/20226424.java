    public void run() {
        try {
            URLConnection urlConnection = (URLConnection) provideResource(m_event);
            m_event.getApplication().getLogger().logMessage(3, "Applet HTTP Server Adapter: Request length is: " + m_request.getRequestBytesLength());
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(m_request.getRequestBytes());
            urlConnection.connect();
            String contentType = urlConnection.getContentType();
            InputStream inputStream = urlConnection.getInputStream();
            byte[] readBuffer = new byte[READ_BUFFER_SIZE];
            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            int i;
            while ((i = inputStream.read(readBuffer)) != -1) {
                responseStream.write(readBuffer, 0, i);
            }
            byte[] response = responseStream.toByteArray();
            int responseLength = response.length;
            m_event.getApplication().getLogger().logMessage(3, "Applet HTTP Server Adapter: Response length is: " + responseLength);
            m_event.getApplication().getLogger().logMessage(8, "Applet HTTP Server Adapter: Response: " + response);
            ((ServerConnection) m_event.getConnection()).setServerResource(this, urlConnection);
            m_event.getConnection().setResponse(new HTTPServerResponse(response, contentType));
        } catch (ServerResourceException e) {
            m_event.getApplication().getLogger().logError(3, "Applet HTTP Server Adapter failed to obtain URL Connection.");
            m_event.getConnection().setError(AppError.ms_serverError);
        } catch (Exception e) {
            m_event.getApplication().getLogger().logError(3, "Applet HTTP Server Adapter failed to write to or read from URL Connection. " + "Exception: " + e);
            m_event.getConnection().setError(AppError.ms_serverError);
        }
    }
