    public void run() {
        HttpURLConnection httpurlconnection = null;
        try {
            URL url = new URL(location);
            httpurlconnection = (HttpURLConnection) url.openConnection();
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(false);
            httpurlconnection.connect();
            if (httpurlconnection.getResponseCode() / 100 == 4) {
                return;
            }
            if (buffer == null) {
                imageData.image = ImageIO.read(httpurlconnection.getInputStream());
            } else {
                imageData.image = buffer.parseUserSkin(ImageIO.read(httpurlconnection.getInputStream()));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpurlconnection.disconnect();
        }
    }
