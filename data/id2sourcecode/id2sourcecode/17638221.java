    protected static URLConnection getUrlConnection(final Context context, final String name, final String id, final boolean fullsize) throws IOException {
        if (!isOnline(context)) return null;
        String imageUrl = "";
        if (fullsize) {
            if (DeaddropDB.HTTPS) imageUrl = DeaddropDB.IMAGE_URL_FS_HTTPS + id + '/' + name; else imageUrl = DeaddropDB.IMAGE_URL_FS + id + '/' + name;
        } else {
            if (DeaddropDB.HTTPS) imageUrl = DeaddropDB.IMAGE_URL_HTTPS + id + '/' + name; else imageUrl = DeaddropDB.IMAGE_URL + id + '/' + name;
        }
        URL url;
        URLConnection ucon;
        try {
            url = new URL(imageUrl);
            ucon = url.openConnection();
        } catch (final MalformedURLException e) {
            Log.v(TAG, "Malformed URL " + imageUrl);
            Log.v(TAG, "" + e);
            return null;
        } catch (final IOException e) {
            Log.v(TAG, "Problem accessing url " + imageUrl);
            Log.v(TAG, "" + e);
            return null;
        }
        return ucon;
    }
