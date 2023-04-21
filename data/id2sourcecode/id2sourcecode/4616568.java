    public void runReport(ResourceCollection collection, List<String> resourceNames, OutputStream out) throws IOException {
        InputStream in = collection.getBackupInputStream();
        FileUtils.copyFile(in, out);
        in.close();
    }
