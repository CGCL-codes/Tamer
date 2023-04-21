    public static Drawable getUserIcon(URL url) {
        if (null != url) {
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                return Drawable.createFromStream(conn.getInputStream(), "image");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
