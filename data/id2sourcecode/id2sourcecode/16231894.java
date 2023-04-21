    public Bitmap getBitmap(Boolean useCache) {
        if (cache != null && useCache) return cache;
        if (url == null) return null;
        Bitmap bm = null;
        try {
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
        }
        if (bm != null) cache = bm;
        return bm;
    }
