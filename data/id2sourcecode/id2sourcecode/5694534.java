    public static Drawable loadImageFromUrl(String url) {
        InputStream inputStream;
        try {
            inputStream = new URL(url).openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Drawable.createFromStream(inputStream, "src");
    }
