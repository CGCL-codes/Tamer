    private Bitmap loadBitmapSynchronous(String imageUrl, boolean force) {
        File file = getCacheFile(imageUrl);
        if (!force && file.canRead()) {
            if (Spark.LOGV) Log.v(Spark.LOG_TAG, "Cache hit: " + file.getAbsolutePath());
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }
        byte[] blob;
        int length;
        try {
            URL url = new URL(imageUrl);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream stream = conn.getInputStream();
            try {
                if ((length = conn.getContentLength()) <= 0) return null;
                blob = new byte[length];
                for (int r = 0; r < length; r += stream.read(blob, r, length - r)) ;
            } finally {
                if (stream != null) stream.close();
            }
        } catch (IOException e) {
            if (Spark.LOGV) e.printStackTrace();
            return null;
        }
        {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(blob);
                fos.close();
                if (Spark.LOGV) Log.v(Spark.LOG_TAG, "Wrote to cache: " + file.getAbsolutePath());
            } catch (IOException e) {
                if (Spark.LOGV) e.printStackTrace();
            }
        }
        return BitmapFactory.decodeByteArray(blob, 0, length);
    }
