    public static LImage loadWebImage(String string, boolean transparency) {
        LImage img = null;
        try {
            java.net.URL url = new java.net.URL(string);
            java.net.HttpURLConnection http = (java.net.HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.connect();
            InputStream is = http.getInputStream();
            img = GraphicsUtils.loadImage(is, transparency);
            if (img.getWidth() == 0 || img.getHeight() == 0) {
                img = null;
            }
            is.close();
        } catch (Exception e) {
            throw new RuntimeException(("File not found. ( " + string + " )").intern());
        }
        return img;
    }
