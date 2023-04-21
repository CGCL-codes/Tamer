    public String getName() {
        String formData = "server=id";
        try {
            URL server = new URL("http", host, port, path + "?" + formData);
            logger.log(Level.INFO, "connecting to {0}", server);
            URLConnection urlConnection = server.openConnection();
            urlConnection.connect();
            InputStream in = urlConnection.getInputStream();
            String result = new String(read(in));
            logger.log(Level.INFO, "response={0}", result);
            return result;
        } catch (IOException e) {
            return "";
        }
    }
