    public static Bitmap download_picture_thumb(String sessionid, String key) {
        if (thumb_cache.containsKey(key)) {
            return thumb_cache.get(key);
        }
        OutputStream os = null;
        String urlString = "https://mt0-s2.cloud.cm/rpc/raw?c=Pictures&m=download_picture_thumb&thumb_size=medium&key=" + key;
        Bitmap bitmap = null;
        ArrayList files = new ArrayList();
        try {
            URL url = new URL(urlString);
            Log.d("current running function name:", "download_picture_thumb");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Cookie", "PHPSESSID=" + sessionid);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            InputStream is = conn.getInputStream();
            Log.d("size of the picture in download_picture_thumb", "" + is.available());
            bitmap = BitmapFactory.decodeStream(is);
            thumb_cache.put(key, bitmap);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
