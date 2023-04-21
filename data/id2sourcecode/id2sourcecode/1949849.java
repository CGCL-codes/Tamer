    public static UrlAssetInfo create(AssetManager assetManager, AssetKey key, URL url) throws IOException {
        URLConnection conn = url.openConnection();
        conn.setUseCaches(false);
        InputStream in = conn.getInputStream();
        if (in == null) {
            return null;
        } else {
            return new UrlAssetInfo(assetManager, key, url, in);
        }
    }
