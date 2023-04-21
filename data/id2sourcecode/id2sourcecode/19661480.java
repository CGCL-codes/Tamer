    private Bitmap downloadImage(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String fileName = writeToFile(getMD5(urlStr), conn.getInputStream());
            return BitmapFactory.decodeFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
