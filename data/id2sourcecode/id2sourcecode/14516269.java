    public static void writeUrlToFile(URL url, File file, IProgressMonitor progressMonitor) throws IOException {
        URLConnection conn = url.openConnection();
        int totalSize = conn.getContentLength();
        FileUtilities.writeInputStreamToFile(conn.getInputStream(), file, totalSize, progressMonitor);
    }
