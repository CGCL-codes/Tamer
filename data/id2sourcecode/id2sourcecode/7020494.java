    public void run() {
        try {
            URLConnection conn = (new URL(url)).openConnection();
            conn.connect();
            Bitmap bm = BitmapFactory.decodeStream(conn.getInputStream());
            if (bm == null) {
                ImageDisplayer displayer = new ImageDisplayer(view, null, pbar, display);
                callback.onImageReceived(displayer);
            } else {
                ImageDisplayer displayer = new ImageDisplayer(view, bm, pbar, display);
                callback.onImageReceived(displayer);
            }
        } catch (IOException e) {
            ImageDisplayer displayer = new ImageDisplayer(view, null, pbar, display);
            callback.onImageReceived(displayer);
            Log.e(TAG, "Error downloading bitmap: " + url, e);
        }
    }
