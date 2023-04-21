    public static void addImageToZip(Image image, String entryName, ZipOutputStream zOut, int format, IProgressMonitor progressMonitor) throws IOException {
        ZipEntry zipEntry = new ZipEntry(entryName);
        try {
            zOut.putNextEntry(zipEntry);
        } catch (IOException ex) {
            return;
        }
        ImageLoader loader = new ImageLoader();
        loader.data = new ImageData[] { image.getImageData() };
        loader.save(zOut, format);
        zOut.closeEntry();
    }
