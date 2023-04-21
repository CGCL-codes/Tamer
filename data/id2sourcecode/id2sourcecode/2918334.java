    public boolean isFound(URL url) {
        try {
            URLConnection connection = url.openConnection();
            if (connection instanceof HttpURLConnection) {
                HttpURLConnection hconn = (HttpURLConnection) connection;
                hconn.setRequestMethod("HEAD");
                hconn.setDoOutput(false);
                hconn.connect();
                InputStream in = connection.getInputStream();
                byte[] buf = new byte[BUFSIZ];
                while (in.read(buf) >= 0) {
                }
                in.close();
                return hconn.getResponseCode() == 200;
            } else {
                connection.connect();
                InputStream in = connection.getInputStream();
                byte[] buf = new byte[BUFSIZ];
                while (in.read(buf) >= 0) {
                }
                in.close();
                return true;
            }
        } catch (IOException e) {
            return false;
        }
    }
