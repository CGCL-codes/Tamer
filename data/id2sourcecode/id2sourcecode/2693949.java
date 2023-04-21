    public boolean updateFavicon(long id, URL iconUrl) {
        InputStream stream = null;
        OutputStream ico = null;
        boolean r = false;
        ContentURI feedUri = RSSReader.Channels.CONTENT_URI.addId(id);
        ContentURI iconUri = feedUri.addPath("icon");
        try {
            stream = iconUrl.openStream();
            ico = mContent.openOutputStream(iconUri);
            byte[] b = new byte[1024];
            int n;
            while ((n = stream.read(b)) != -1) ico.write(b, 0, n);
            r = true;
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
        } finally {
            try {
                if (stream != null) stream.close();
                if (ico != null) ico.close();
            } catch (IOException e) {
            }
        }
        return r;
    }
