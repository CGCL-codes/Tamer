    public static boolean isUriResolvable(URI uri) throws IOException {
        try {
            if (uri.isAbsolute()) {
                URL url = uri.toURL();
                URLConnection connection = url.openConnection();
                System.out.println("Opened connection");
                connection.setConnectTimeout(100);
                System.out.println("Set timeout to " + connection.getConnectTimeout());
                connection.connect();
                System.out.println("Connecting...");
                long time = System.currentTimeMillis();
                if (connection instanceof HttpURLConnection) {
                    int response = ((HttpURLConnection) connection).getResponseCode();
                    System.out.println("(HTTP) took " + (System.currentTimeMillis() - time) + " ms");
                    if (response == HttpURLConnection.HTTP_OK) {
                        System.out.println("OK");
                        return true;
                    }
                } else {
                    System.out.println("(FILE) took " + (System.currentTimeMillis() - time) + " ms");
                    if (connection.getContentLength() != -1) {
                        System.out.println("OK");
                        return true;
                    }
                }
            }
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
