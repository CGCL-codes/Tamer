    void setURLObject(URL url, boolean forceLoad) {
        if (url != null) {
            if (urlString != null || inputStream != null) throw new IllegalArgumentException(J3dI18N.getString("MediaContainer5"));
            try {
                InputStream stream;
                stream = url.openStream();
                stream.close();
            } catch (Exception e) {
                throw new SoundException(javax.media.j3d.J3dI18N.getString("MediaContainer0"));
            }
        }
        this.url = url;
        if (forceLoad) dispatchMessage();
    }
