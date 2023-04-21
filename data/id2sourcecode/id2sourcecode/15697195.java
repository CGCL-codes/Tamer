    private static List<DownloadUnit> buildQueue(final File directory, final File outDir, final Ui ui) throws IOException {
        final URI uri = directory.toURI();
        final URL url = uri.toURL();
        final String contentType = url.openConnection().getContentType();
        ui.println("URI: {0}", uri);
        ui.println("URL: {0}", url);
        if (!directory.exists()) {
            ui.println("No such file or directory.");
            return Collections.emptyList();
        }
        if (!directory.isDirectory()) {
            ui.println("Not a directory.");
            return Collections.emptyList();
        }
        final long inFileLength = directory.length();
        final String basename = directory.getName();
        final File newOutDir = new File(outDir, basename);
        ui.println("Downloading {0} ({1}, {2} byte(s)) to {3}", url, contentType, Long.valueOf(inFileLength), newOutDir);
        final File inFiles[] = directory.listFiles();
        if (inFiles.length == 0) {
            return Collections.emptyList();
        }
        final List<DownloadUnit> queue = new ArrayList<DownloadUnit>();
        for (final File inFile : inFiles) {
            if (inFile.isDirectory()) {
                queue.addAll(buildQueue(inFile, newOutDir, ui));
            } else {
                queue.add(new DownloadUnit(inFile, newOutDir));
            }
        }
        return queue;
    }
