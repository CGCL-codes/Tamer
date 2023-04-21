    public static void syncDirectories(final File src, final FilenameFilter filter, final File tgt) throws IOException {
        Set<String> srcFilenames = null;
        do {
            srcFilenames = new HashSet<String>(Arrays.asList(src.list(filter)));
            List<String> tgtFilenames = Arrays.asList(tgt.list(filter));
            srcFilenames.removeAll(tgtFilenames);
            if (srcFilenames.size() > 0) {
                int count = FileUtils.copyFiles(src, srcFilenames, tgt);
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Copied " + count);
                }
            }
        } while (srcFilenames != null && srcFilenames.size() > 0);
    }
