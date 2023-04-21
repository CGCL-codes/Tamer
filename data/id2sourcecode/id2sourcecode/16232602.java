    private Bitmap getBitmap(String url) {
        try {
            String filename = String.valueOf(url.hashCode());
            File f = new File(cacheDir, filename);
            Bitmap b = decodeFile(f);
            if (b != null) {
                return b;
            }
            Bitmap bitmap = null;
            InputStream is = new URL(url).openStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            is.close();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
