    public void run() throws UnknownHostException, IOException {
        URL url = new URL(getUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream is = connection.getInputStream();
        try {
            int oneChar;
            while ((oneChar = is.read()) != -1) {
                System.out.print((char) oneChar);
            }
        } finally {
            is.close();
        }
        connection.disconnect();
    }
