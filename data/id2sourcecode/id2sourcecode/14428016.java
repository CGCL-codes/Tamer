    public static long getLastModified(URL url) throws IOException {
        if ("file".equals(url.getProtocol())) {
            File file = new File(url.getFile());
            return file.lastModified();
        } else if ("jar".equals(url.getProtocol())) {
            String filenName = url.getFile();
            if (filenName != null && filenName.contains("!/")) {
                String[] tokens = filenName.split("!/");
                String entryName = tokens[tokens.length - 1];
                JarURLConnection jarUrl = (JarURLConnection) url.openConnection();
                ZipEntry entry = jarUrl.getJarFile().getEntry(entryName);
                if (entry != null) {
                    return entry.getTime();
                }
            }
        } else {
            throw new IllegalArgumentException("Die Url '" + url.toString() + "' zeigt nicht auf eine Datei");
        }
        return 0L;
    }
