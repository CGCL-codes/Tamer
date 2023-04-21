    public Drawable loadImageFromUrl(String url) {
        InputStream inputStream = null;
        Drawable image = null;
        try {
            inputStream = new URL(url).openStream();
            image = Drawable.createFromStream(inputStream, "src");
        } catch (Exception e) {
            Log.e(ctGlobal.tag, "ctImageLoader.loadImageFromUrl: " + e.toString());
        }
        return image;
    }
