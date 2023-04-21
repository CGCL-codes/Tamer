    public String getExtensionForMimetype(String mimetype) {
        if (mimetypeToExt == null) {
            mimetypeToExt = new Properties();
            try {
                URL url = peer.getResource("filerepository", "mimetypes.properties");
                if (url != null) {
                    mimetypeToExt.load(url.openStream());
                }
            } catch (java.io.IOException ex) {
                return peer.getString("filerepository", "Mimetype.Extension.unknown");
            }
        }
        return mimetypeToExt.getProperty(mimetype, peer.getString("filerepository", "Mimetype.Extension.unknown"));
    }
