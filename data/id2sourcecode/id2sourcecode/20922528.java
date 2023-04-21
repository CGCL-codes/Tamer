
    public static String getCacheFilename(String filename) {
        String cache_dir = "cache/";
        if (netscape) {
            cache_dir += "netscape/";
        }
        String zip_filename = cache_dir + filename + ".zip";
        zip_filename = zip_filename.replace(':', '_');
        zip_filename = zip_filename.replace('|', '_');
        return zip_filename;
    }

    public boolean existsCacheFile(String filename) {
        boolean exists = false;
        try {
            URL url = new URL(base_url, getCacheFilename(filename));
            String fullfilename = makeLocalFilenameFromURL(url);
            exists = new File(fullfilename).exists();
        } catch (Exception e) {
        }
