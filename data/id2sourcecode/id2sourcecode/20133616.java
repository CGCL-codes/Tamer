    private boolean checkUrl(String arg) {
        HttpURLConnection.setFollowRedirects(true);
        String urlString = arg;
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            if (connection instanceof HttpURLConnection) {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.connect();
                int response = httpConnection.getResponseCode();
                String s1 = Integer.toString(response);
                if (s1.charAt(0) == '2') {
                    return true;
                }
                InputStream is = httpConnection.getInputStream();
                byte[] buffer = new byte[256];
                while (is.read(buffer) != -1) {
                }
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
