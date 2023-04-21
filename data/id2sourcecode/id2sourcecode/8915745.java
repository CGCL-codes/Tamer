    public static boolean copyResourcesRecursively(final URL originUrl, final File destination) {
        try {
            final URLConnection urlConnection = originUrl.openConnection();
            if (urlConnection instanceof JarURLConnection) {
                return FileUtils.copyJarResourcesRecursively(destination, (JarURLConnection) urlConnection);
            } else {
                return FileUtils.copyFilesRecusively(new File(originUrl.getPath()), destination);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return false;
    }
