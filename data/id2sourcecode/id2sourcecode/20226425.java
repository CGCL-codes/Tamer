    protected synchronized Object createResource(AppEvent event) throws ServerResourceException {
        try {
            ServerConnection connection = (ServerConnection) event.getConnection();
            Object applet = m_event.getApplication().getCallBackObject();
            if (!(applet instanceof BeanstalkApplet)) {
                throw new Exception("AppletHTTPServerAdapter: Not executing in applet environment.");
            }
            URLConnection urlConnection = new URL(((BeanstalkApplet) applet).getDocumentBase(), connection.getServerID()).openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Method", m_request.getMethod());
            urlConnection.setRequestProperty("Content-type", m_request.getContentType());
            return urlConnection;
        } catch (Exception e) {
            m_event.getApplication().getLogger().logError(3, "Applet HTTP Server Adapter failed create URL Connection. " + "Exception: " + e);
            m_event.getConnection().setError(AppError.ms_serverError);
            throw new ServerResourceException();
        }
    }
