    @Override
    protected Bitmap doInBackground(String... inputUrls) {
        Log.d(Constants.LOG_TAG, "making HTTP trip for image:" + inputUrls[0]);
        Bitmap bitmap = null;
        try {
            URL url = new URL(inputUrls[0]);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(5000);
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            if (bitmap != null) {
                bitmap = ImageUtil.getRoundedCornerBitmap(bitmap, 12);
                cache.put(inputUrls[0], bitmap);
            }
        } catch (MalformedURLException e) {
            Log.e(Constants.LOG_TAG, "Exception loading image, malformed URL", e);
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "Exception loading image, IO error", e);
        }
        return bitmap;
    }
