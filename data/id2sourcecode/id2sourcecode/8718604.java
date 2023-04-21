    public static byte[] service(SessionCookies session, URL serviceURL, String service, byte[] request, Decoder decoder, Proxy proxy) throws IOException {
        URLConnection urlConnection = new URL(serviceURL.toString() + service).openConnection(proxy);
        if (!(urlConnection instanceof HttpURLConnection)) throw new ProtocolException("Not a HTTP service url.");
        byte[] response = null;
        HttpURLConnection connection = (HttpURLConnection) urlConnection;
        try {
            if (service.equals(CONNECT)) response = connect(connection); else if (service.startsWith(SERVICE)) response = service(connection, session, request, decoder); else if (service.equals(DISCONNECT)) disconnect(connection, session); else throw new IOException("Service request is invalid!");
        } finally {
            if (connection != null) connection.disconnect();
        }
        return response;
    }
