    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        cacheDir.mkdirs();
        File cache = new File(cacheDir, IO.fixFileName(publicId, true));
        if (!cache.exists()) {
            try {
                URL url = new URL(systemId);
                URLConnection connection = url.openConnection();
                connection.setRequestProperty("User-Agent", "Cromoteca Cached Entity Resolver");
                InputStream is = connection.getInputStream();
                OutputStream os = new FileOutputStream(cache);
                IO.copyStream(is, os, true);
            } catch (IOException ex) {
                Context.log("Error while fetching entity", ex);
                return null;
            }
        }
        InputSource inputSource = new InputSource(systemId);
        inputSource.setPublicId(publicId);
        inputSource.setByteStream(new FileInputStream(cache));
        return inputSource;
    }
