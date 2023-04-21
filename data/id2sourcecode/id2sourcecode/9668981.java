    private static Image getPluginImageFromUrl(URL url) {
        try {
            try {
                String key = url.toExternalForm();
                Image image = m_URLImageMap.get(key);
                if (image == null) {
                    InputStream stream = url.openStream();
                    try {
                        image = getImage(stream);
                        m_URLImageMap.put(key, image);
                    } finally {
                        stream.close();
                    }
                }
                return image;
            } catch (Throwable e) {
            }
        } catch (Throwable e) {
        }
        return null;
    }
