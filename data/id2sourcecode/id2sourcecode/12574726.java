    public ImageIcon getLogo() {
        String formData = "server=logo";
        try {
            URL server = new URL("http", host, port, path + "?" + formData);
            logger.log(Level.INFO, "connecting to {0}", server);
            URLConnection urlConnection = server.openConnection();
            urlConnection.connect();
            InputStream in = urlConnection.getInputStream();
            byte[] data = read(in);
            logger.log(Level.INFO, "response={0} bytes", data.length);
            return new ImageIcon(data);
        } catch (IOException e) {
            return new ImageIcon();
        }
    }
