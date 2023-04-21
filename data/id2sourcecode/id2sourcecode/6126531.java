    public Bitmap loadImageFromNetwork(final String urlstr) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(new URL(urlstr).openStream());
            Log.i("ImageLoader::loadImageFromCache", "We found a image cache file and load it.");
            return bitmap;
        } catch (Exception e) {
            Log.w("ImageLoader::loadImageFromCache", "No resource in cache: " + e.getMessage());
        }
        return null;
    }
