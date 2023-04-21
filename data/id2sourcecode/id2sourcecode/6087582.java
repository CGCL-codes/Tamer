    private final InputStream connect(ApiUrl urlBase, ParameterString parameters) throws JEveConnectionException {
        URL url = null;
        URLConnection connection = null;
        Writer out = null;
        try {
            url = new URL(urlBase.getUrl());
            connection = url.openConnection();
            if (parameters != null) {
                connection.setDoOutput(true);
                connection.setConnectTimeout(5000);
                connection.connect();
                out = new OutputStreamWriter(connection.getOutputStream());
                out.write(parameters.toString());
                out.flush();
            }
            return connection.getInputStream();
        } catch (MalformedURLException mue) {
            throw new JEveConnectionException("Invalid url", mue);
        } catch (SocketTimeoutException ste) {
            throw new JEveConnectionException("Proxy configuration error", ste);
        } catch (IOException ioe) {
            throw new JEveConnectionException("Unable to connect to website", ioe);
        } finally {
            try {
                if (out != null) out.close();
            } catch (Exception e) {
                System.err.println("Failed to close stream\n" + e.getMessage());
            }
        }
    }
