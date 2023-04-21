    private URLConnection getPreferredConnection(URL url, boolean closeAfter) throws IOException {
        URLConnection preferredConnection = null;
        if (url.getProtocol().equals("file")) {
            return url.openConnection();
        }
        URLConnection closeConn = null;
        URLConnection conn = null;
        try {
            closeConn = url.openConnection();
            conn = closeConn;
            if (conn instanceof HttpURLConnection) {
                HttpURLConnection hconn = (HttpURLConnection) conn;
                if (closeAfter) {
                    hconn.setRequestMethod("HEAD");
                }
                int responseCode = hconn.getResponseCode();
                switch(responseCode) {
                    case HttpURLConnection.HTTP_OK:
                    case HttpURLConnection.HTTP_NOT_AUTHORITATIVE:
                        break;
                    case HttpURLConnection.HTTP_NOT_FOUND:
                    case HttpURLConnection.HTTP_FORBIDDEN:
                    case HttpURLConnection.HTTP_GONE:
                        conn = null;
                        break;
                    default:
                        throw new IOException("Indefinite http response for " + "preferred list request:" + hconn.getResponseMessage());
                }
            }
        } finally {
            if (closeAfter && (closeConn != null)) {
                try {
                    closeConn.getInputStream().close();
                } catch (IOException e) {
                }
            }
        }
        return conn;
    }
