    @Override
    protected Bitmap doInBackground(String... inputUrls) {
        try {
            URL url = new URL(inputUrls[0]);
            return BitmapFactory.decodeStream(url.openStream());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
