    protected ImageDecoder getDecoder() {
        InputStream is = null;
        try {
            JarURLConnection c = (JarURLConnection) url.openConnection();
            JarFile f = c.getJarFile();
            JarEntry e = c.getJarEntry();
            if (entryName != null && e == null) {
                e = f.getJarEntry(entryName);
            }
            if (e == null || (e != null && entryName != null && (!(entryName.equals(e.getName()))))) {
                return null;
            }
            is = f.getInputStream(e);
        } catch (IOException e) {
            return null;
        }
        ImageDecoder id = decoderForType(is, mimeType);
        if (id == null) {
            id = getDecoder(is);
        }
        return id;
    }
