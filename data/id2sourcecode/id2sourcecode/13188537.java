    public String getName() {
        try {
            URLConnection con = url.openConnection();
            if (con instanceof JarURLConnection) return ((JarURLConnection) con).getEntryName(); else return FileProvider.filePathFromURL(url);
        } catch (IOException e) {
            Assertions.UNREACHABLE();
            return null;
        }
    }
