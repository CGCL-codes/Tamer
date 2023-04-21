    public void setImage(String imageURL) throws IOException {
        URL url = new URL(imageURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.connect();
        InputStream is = conn.getInputStream();
        image = BitmapFactory.decodeStream(is);
    }
