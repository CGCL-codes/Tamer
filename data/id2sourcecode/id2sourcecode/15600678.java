    public static Bitmap getAndSetBitmapFromNet(String urlPath) {
        Bitmap bm = null;
        if (urlPath != null) {
            try {
                BufferedInputStream bis = new BufferedInputStream(new URL(urlPath).openStream(), 1024);
                final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                BufferedOutputStream out = new BufferedOutputStream(dataStream, 1024);
                copy(bis, out);
                out.flush();
                final byte[] data = dataStream.toByteArray();
                bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                Log.i(I, "data.length: " + data.length);
                out.close();
                dataStream.close();
                bis.close();
                bm = processBitmap(bm);
            } catch (IOException e) {
                Log.i(I, "URL Connection or Bitmap processing Exception");
                e.printStackTrace();
            }
        }
        return bm;
    }
