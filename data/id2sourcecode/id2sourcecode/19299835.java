    private void connect() throws ImageSourceException {
        try {
            URL url = new URL(this.url);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            dataInputStream = new DataInputStream(new BufferedInputStream(huc.getInputStream()));
            connected = true;
        } catch (IOException e) {
            throw new ImageSourceException(CONNECT_ERROR, e);
        }
    }
