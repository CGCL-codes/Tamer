    public static boolean copyResourcesRecursively(final URL originUrl, final File destination) {
        try {
            final URLConnection urlConnection = originUrl.openConnection();
            if (urlConnection instanceof JarURLConnection) {
                return FileUtil.copyJarResourcesRecursively(destination, (JarURLConnection) urlConnection);
            } else {
                return FileUtil.copyFilesRecusively(new File(originUrl.getPath()), destination);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return false;
    }
