    public String storeBitmapFromURL(URL url, String pathName, final Context ctx, String tag) {
        try {
            bm = BitmapFactory.decodeStream(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(tag, e.toString(), e);
            return null;
        }
        return storeBitmap(ctx, pathName, tag);
    }
