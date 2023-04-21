    public static File copyAs(File source, File destination) throws IOException {
        destination = resolveDestination(source, destination);
        if (source.isDirectory()) {
            org.apache.commons.io.FileUtils.copyDirectory(source, destination);
        } else {
            org.apache.commons.io.FileUtils.copyFile(source, destination);
        }
        return destination;
    }
