    private static Image getPluginImageFromUrl(final URL url) {
        try {
            try {
                String key = url.toExternalForm();
                Image image = mURLImageMap.get(key);
                if (image == null) {
                    InputStream stream = url.openStream();
                    try {
                        image = getImage(stream);
                        mURLImageMap.put(key, image);
                    } finally {
                        stream.close();
                    }
                }
                return image;
            } catch (Throwable e) {
                e.getMessage();
            }
        } catch (Throwable e) {
            e.getMessage();
        }
        return null;
    }
